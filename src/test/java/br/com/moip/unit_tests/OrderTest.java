package br.com.moip.unit_tests;

import br.com.moip.Moip;
import br.com.moip.models.Setup;
import br.com.moip.unit_tests.setup.SetupFactory;
import br.com.moip.utilities.Parser;
import com.rodrigosaito.mockwebserver.player.Play;
import com.rodrigosaito.mockwebserver.player.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OrderTest {

    @Rule
    public Player player = new Player();
    private Setup setup;
    private Map<String, Object> body;
    private Parser parser;

    @Before
    public void initialize() {
        this.body = new HashMap<>();
        this.setup = new SetupFactory().setup(player.getURL("").toString());
        this.parser = new Parser();
    }

    @Play("order/create")
    @Test
    public void createOrderTest() {

        Map<String, Object> order = Moip.API.orders().create(body, setup);

        assertEquals("ORD-A4HGZXSXARIV", order.get("id"));
        assertEquals("my_order_ownId", order.get("ownId"));
        assertEquals("CREATED", order.get("status"));
        assertEquals("V2", order.get("platform"));
        assertEquals("2018-07-12T14:53:46.683-03", order.get("createdAt"));
        assertEquals("2018-07-12T14:53:46.683-03", order.get("updatedAt"));

        Map<String, Object> amount = parser.objectToMap(order.get("amount"));
        assertEquals(0, amount.get("paid"));
        assertEquals(11000, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(0, amount.get("liquid"));
        assertEquals(0, amount.get("otherReceivers"));
        assertEquals("BRL", amount.get("currency"));

        Map<String, Object> subtotals = parser.objectToMap(amount.get("subtotals"));
        assertEquals(1500, subtotals.get("shipping"));
        assertEquals(0, subtotals.get("addition"));
        assertEquals(0, subtotals.get("discount"));
        assertEquals(9500, subtotals.get("items"));

        List<Map<String, Object>> items = parser.objectToList(order.get("items"));
        assertEquals("Descrição do pedido", items.get(0).get("product"));
        assertEquals(9500, items.get(0).get("price"));
        assertEquals("Camiseta estampada branca", items.get(0).get("detail"));
        assertEquals(1, items.get(0).get("quantity"));
        assertEquals("CLOTHING", items.get(0).get("category"));

        List<Map<String, Object>> addresses = parser.objectToList(order.get("addresses"));
        assertEquals("Itaim Bibi", addresses.get(0).get("district"));
        assertEquals("01451001", addresses.get(0).get("zipCode"));
        assertEquals("3064", addresses.get(0).get("streetNumber"));
        assertEquals("Avenida Brigadeiro Faria Lima", addresses.get(0).get("street"));
        assertEquals("São Paulo", addresses.get(0).get("city"));
        assertEquals("SP", addresses.get(0).get("state"));
        assertEquals("SHIPPING", addresses.get(0).get("type"));
        assertEquals("BRA", addresses.get(0).get("country"));

        Map<String, Object> customer = parser.objectToMap(order.get("customer"));
        assertEquals("CUS-35TX8MWZ6SVX", customer.get("id"));
        assertEquals("moip_create_customer_unit_test", customer.get("ownId"));
        assertEquals("SDK Java", customer.get("fullname"));
        assertEquals("2018-07-11T11:57:22.000-03", customer.get("createdAt"));
        assertEquals("2018-07-12T14:53:46.687-03", customer.get("updatedAt"));
        assertEquals("1980-05-10", customer.get("birthDate"));
        assertEquals("sdk.java@moip.com.br", customer.get("email"));

        Map<String, Object> fundingInstrument = parser.objectToMap(customer.get("fundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-7GKFB3S2R8SS", creditCard.get("id"));
        assertEquals("ELO", creditCard.get("brand"));
        assertEquals("636297", creditCard.get("first6"));
        assertEquals("7013", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> phone = parser.objectToMap(customer.get("phone"));
        assertEquals("55", phone.get("countryCode"));
        assertEquals("11", phone.get("areaCode"));
        assertEquals("22226842", phone.get("number"));

        Map<String, Object> taxDocument = parser.objectToMap(customer.get("taxDocument"));
        assertEquals("CPF", taxDocument.get("type"));
        assertEquals("10013390023", taxDocument.get("number"));

        List<Map<String, Object>> addressesCustomer = parser.objectToList(customer.get("addresses"));
        assertEquals("Itaim Bibi", addressesCustomer.get(0).get("district"));
        assertEquals("01451001", addressesCustomer.get(0).get("zipCode"));
        assertEquals("3064", addressesCustomer.get(0).get("streetNumber"));
        assertEquals("Avenida Brigadeiro Faria Lima", addressesCustomer.get(0).get("street"));
        assertEquals("São Paulo", addressesCustomer.get(0).get("city"));
        assertEquals("SP", addressesCustomer.get(0).get("state"));
        assertEquals("SHIPPING", addressesCustomer.get(0).get("type"));
        assertEquals("BRA", addressesCustomer.get(0).get("country"));

        Map<String, Object> shippingAddress = parser.objectToMap(customer.get("shippingAddress"));
        assertEquals("01451001", shippingAddress.get("zipCode"));
        assertEquals("Avenida Brigadeiro Faria Lima", shippingAddress.get("street"));
        assertEquals("3064", shippingAddress.get("streetNumber"));
        assertEquals("São Paulo", shippingAddress.get("city"));
        assertEquals("Itaim Bibi", shippingAddress.get("district"));
        assertEquals("SP", shippingAddress.get("state"));
        assertEquals("BRA", shippingAddress.get("country"));

        Map<String, Object> moipAccount = parser.objectToMap(customer.get("moipAccount"));
        assertEquals("MPA-8C2107EC8CCB", moipAccount.get("id"));

        Map<String, Object> linksCustomer = parser.objectToMap(customer.get("_links"));
        Map<String, Object> selfCustomer = parser.objectToMap(linksCustomer.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-35TX8MWZ6SVX", selfCustomer.get("href"));

        Map<String, Object> hostedAccountCustomer = parser.objectToMap(linksCustomer.get("hostedAccount"));
        assertEquals("https://hostedaccount-sandbox.moip.com.br?token=69ec54c3-c6a1-4f04-953b-af7d0f0fbee0&id=CUS-35TX8MWZ6SVX&mpa=MPA-CULBBYHD11", hostedAccountCustomer.get("redirectHref"));

        List<Map<String, Object>> fundingInstruments = parser.objectToList(customer.get("fundingInstruments"));
        Map<String, Object> creditCardCustomer = parser.objectToMap(fundingInstruments.get(0).get("creditCard"));
        assertEquals("CRC-7GKFB3S2R8SS", creditCardCustomer.get("id"));
        assertEquals("ELO", creditCardCustomer.get("brand"));
        assertEquals("636297", creditCardCustomer.get("first6"));
        assertEquals("7013", creditCardCustomer.get("last4"));
        assertEquals(true, creditCardCustomer.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments.get(0).get("method"));

        List<Map<String, Object>> payments = parser.objectToList(order.get("payments"));
        assertTrue(payments.isEmpty());

        List<Map<String, Object>> escrows = parser.objectToList(order.get("escrows"));
        assertTrue(escrows.isEmpty());

        List<Map<String, Object>> refunds = parser.objectToList(order.get("refunds"));
        assertTrue(refunds.isEmpty());

        List<Map<String, Object>> entries = parser.objectToList(order.get("entries"));
        assertTrue(entries.isEmpty());

        List<Map<String, Object>> events = parser.objectToList(order.get("events"));
        assertEquals("ORDER.CREATED", events.get(0).get("type"));
        assertEquals("2018-07-12T14:53:46.683-03", events.get(0).get("createdAt"));
        assertEquals("", events.get(0).get("description"));

        List<Map<String, Object>> receivers = parser.objectToList(order.get("receivers"));
        Map<String, Object> moipAccountReceiver = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccountReceiver.get("id"));
        assertEquals("integracao@labs.moip.com.br", moipAccountReceiver.get("login"));
        assertEquals("Moip SandBox", moipAccountReceiver.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> amountReceiver = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(11000, amountReceiver.get("total"));
        assertEquals("BRL", amountReceiver.get("currency"));
        assertEquals(0, amountReceiver.get("fees"));
        assertEquals(0, amountReceiver.get("refunds"));

        assertEquals(true, receivers.get(0).get("feePayor"));

        Map<String, Object> links = parser.objectToMap(order.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-A4HGZXSXARIV", self.get("href"));

        Map<String, Object> checkout = parser.objectToMap(links.get("checkout"));
        Map<String, Object> payCheckout = parser.objectToMap(checkout.get("payCheckout"));
        assertEquals("https://checkout-new-sandbox.moip.com.br?token=82678972-25ad-4d20-951a-ac9e384a6c04&id=ORD-A4HGZXSXARIV", payCheckout.get("redirectHref"));

        Map<String, Object> payCreditCard = parser.objectToMap(checkout.get("payCreditCard"));
        assertEquals("https://checkout-new-sandbox.moip.com.br?token=82678972-25ad-4d20-951a-ac9e384a6c04&id=ORD-A4HGZXSXARIV&payment-method=credit-card", payCreditCard.get("redirectHref"));

        Map<String, Object> payBoleto = parser.objectToMap(checkout.get("payBoleto"));
        assertEquals("https://checkout-new-sandbox.moip.com.br?token=82678972-25ad-4d20-951a-ac9e384a6c04&id=ORD-A4HGZXSXARIV&payment-method=boleto", payBoleto.get("redirectHref"));

        Map<String, Object> payOnlineBankDebitItau = parser.objectToMap(checkout.get("payOnlineBankDebitItau"));
        assertEquals("https://checkout-sandbox.moip.com.br/debit/itau/ORD-A4HGZXSXARIV", payOnlineBankDebitItau.get("redirectHref"));
    }

    @Play("order/get")
    @Test
    public void getOrderTest() {

        Map<String, Object> order = Moip.API.orders().get("ORD-A4HGZXSXARIV", setup);

        assertEquals("ORD-A4HGZXSXARIV", order.get("id"));
        assertEquals("my_order_ownId", order.get("ownId"));
        assertEquals("CREATED", order.get("status"));
        assertEquals("V2", order.get("platform"));
        assertEquals("2018-07-12T14:53:46.000-03", order.get("createdAt"));
        assertEquals("2018-07-12T14:53:46.000-03", order.get("updatedAt"));

        Map<String, Object> amount = parser.objectToMap(order.get("amount"));
        assertEquals(0, amount.get("paid"));
        assertEquals(11000, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(0, amount.get("liquid"));
        assertEquals(0, amount.get("otherReceivers"));
        assertEquals("BRL", amount.get("currency"));

        Map<String, Object> subtotals = parser.objectToMap(amount.get("subtotals"));
        assertEquals(1500, subtotals.get("shipping"));
        assertEquals(0, subtotals.get("addition"));
        assertEquals(0, subtotals.get("discount"));
        assertEquals(9500, subtotals.get("items"));

        List<Map<String, Object>> items = parser.objectToList(order.get("items"));
        assertEquals("Descrição do pedido", items.get(0).get("product"));
        assertEquals(9500, items.get(0).get("price"));
        assertEquals("Camiseta estampada branca", items.get(0).get("detail"));
        assertEquals(1, items.get(0).get("quantity"));
        assertEquals("CLOTHING", items.get(0).get("category"));

        List<Map<String, Object>> addresses = parser.objectToList(order.get("addresses"));
        assertEquals("Itaim Bibi", addresses.get(0).get("district"));
        assertEquals("01451001", addresses.get(0).get("zipCode"));
        assertEquals("3064", addresses.get(0).get("streetNumber"));
        assertEquals("Avenida Brigadeiro Faria Lima", addresses.get(0).get("street"));
        assertEquals("São Paulo", addresses.get(0).get("city"));
        assertEquals("SP", addresses.get(0).get("state"));
        assertEquals("SHIPPING", addresses.get(0).get("type"));
        assertEquals("BRA", addresses.get(0).get("country"));

        Map<String, Object> customer = parser.objectToMap(order.get("customer"));
        assertEquals("CUS-35TX8MWZ6SVX", customer.get("id"));
        assertEquals("moip_create_customer_unit_test", customer.get("ownId"));
        assertEquals("SDK Java", customer.get("fullname"));
        assertEquals("2018-07-11T11:57:22.000-03", customer.get("createdAt"));
        assertEquals("2018-07-12T14:53:46.000-03", customer.get("updatedAt"));
        assertEquals("1980-05-10", customer.get("birthDate"));
        assertEquals("sdk.java@moip.com.br", customer.get("email"));

        Map<String, Object> fundingInstrument = parser.objectToMap(customer.get("fundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-7GKFB3S2R8SS", creditCard.get("id"));
        assertEquals("ELO", creditCard.get("brand"));
        assertEquals("636297", creditCard.get("first6"));
        assertEquals("7013", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> phone = parser.objectToMap(customer.get("phone"));
        assertEquals("55", phone.get("countryCode"));
        assertEquals("11", phone.get("areaCode"));
        assertEquals("22226842", phone.get("number"));

        Map<String, Object> taxDocument = parser.objectToMap(customer.get("taxDocument"));
        assertEquals("CPF", taxDocument.get("type"));
        assertEquals("10013390023", taxDocument.get("number"));

        List<Map<String, Object>> addressesCustomer = parser.objectToList(customer.get("addresses"));
        assertEquals("Itaim Bibi", addressesCustomer.get(0).get("district"));
        assertEquals("01451001", addressesCustomer.get(0).get("zipCode"));
        assertEquals("3064", addressesCustomer.get(0).get("streetNumber"));
        assertEquals("Avenida Brigadeiro Faria Lima", addressesCustomer.get(0).get("street"));
        assertEquals("São Paulo", addressesCustomer.get(0).get("city"));
        assertEquals("SP", addressesCustomer.get(0).get("state"));
        assertEquals("SHIPPING", addressesCustomer.get(0).get("type"));
        assertEquals("BRA", addressesCustomer.get(0).get("country"));

        Map<String, Object> shippingAddress = parser.objectToMap(customer.get("shippingAddress"));
        assertEquals("01451001", shippingAddress.get("zipCode"));
        assertEquals("Avenida Brigadeiro Faria Lima", shippingAddress.get("street"));
        assertEquals("3064", shippingAddress.get("streetNumber"));
        assertEquals("São Paulo", shippingAddress.get("city"));
        assertEquals("Itaim Bibi", shippingAddress.get("district"));
        assertEquals("SP", shippingAddress.get("state"));
        assertEquals("BRA", shippingAddress.get("country"));

        Map<String, Object> moipAccount = parser.objectToMap(customer.get("moipAccount"));
        assertEquals("MPA-8C2107EC8CCB", moipAccount.get("id"));

        Map<String, Object> linksCustomer = parser.objectToMap(customer.get("_links"));
        Map<String, Object> selfCustomer = parser.objectToMap(linksCustomer.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-35TX8MWZ6SVX", selfCustomer.get("href"));

        Map<String, Object> hostedAccountCustomer = parser.objectToMap(linksCustomer.get("hostedAccount"));
        assertEquals("https://hostedaccount-sandbox.moip.com.br?token=69ec54c3-c6a1-4f04-953b-af7d0f0fbee0&id=CUS-35TX8MWZ6SVX&mpa=MPA-CULBBYHD11", hostedAccountCustomer.get("redirectHref"));

        List<Map<String, Object>> fundingInstruments = parser.objectToList(customer.get("fundingInstruments"));
        Map<String, Object> creditCardCustomer = parser.objectToMap(fundingInstruments.get(0).get("creditCard"));
        assertEquals("CRC-7GKFB3S2R8SS", creditCardCustomer.get("id"));
        assertEquals("ELO", creditCardCustomer.get("brand"));
        assertEquals("636297", creditCardCustomer.get("first6"));
        assertEquals("7013", creditCardCustomer.get("last4"));
        assertEquals(true, creditCardCustomer.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments.get(0).get("method"));

        List<Map<String, Object>> payments = parser.objectToList(order.get("payments"));
        assertTrue(payments.isEmpty());

        List<Map<String, Object>> escrows = parser.objectToList(order.get("escrows"));
        assertTrue(escrows.isEmpty());

        List<Map<String, Object>> refunds = parser.objectToList(order.get("refunds"));
        assertTrue(refunds.isEmpty());

        List<Map<String, Object>> entries = parser.objectToList(order.get("entries"));
        assertTrue(entries.isEmpty());

        List<Map<String, Object>> events = parser.objectToList(order.get("events"));
        assertEquals("ORDER.CREATED", events.get(0).get("type"));
        assertEquals("2018-07-12T14:53:46.000-03", events.get(0).get("createdAt"));
        assertEquals("", events.get(0).get("description"));

        List<Map<String, Object>> receivers = parser.objectToList(order.get("receivers"));
        Map<String, Object> moipAccountReceiver = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccountReceiver.get("id"));
        assertEquals("integracao@labs.moip.com.br", moipAccountReceiver.get("login"));
        assertEquals("Moip SandBox", moipAccountReceiver.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> amountReceiver = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(11000, amountReceiver.get("total"));
        assertEquals("BRL", amountReceiver.get("currency"));
        assertEquals(0, amountReceiver.get("fees"));
        assertEquals(0, amountReceiver.get("refunds"));

        assertEquals(true, receivers.get(0).get("feePayor"));

        Map<String, Object> links = parser.objectToMap(order.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-A4HGZXSXARIV", self.get("href"));

        Map<String, Object> checkout = parser.objectToMap(links.get("checkout"));
        Map<String, Object> payCheckout = parser.objectToMap(checkout.get("payCheckout"));
        assertEquals("https://checkout-new-sandbox.moip.com.br?token=82678972-25ad-4d20-951a-ac9e384a6c04&id=ORD-A4HGZXSXARIV", payCheckout.get("redirectHref"));

        Map<String, Object> payCreditCard = parser.objectToMap(checkout.get("payCreditCard"));
        assertEquals("https://checkout-new-sandbox.moip.com.br?token=82678972-25ad-4d20-951a-ac9e384a6c04&id=ORD-A4HGZXSXARIV&payment-method=credit-card", payCreditCard.get("redirectHref"));

        Map<String, Object> payBoleto = parser.objectToMap(checkout.get("payBoleto"));
        assertEquals("https://checkout-new-sandbox.moip.com.br?token=82678972-25ad-4d20-951a-ac9e384a6c04&id=ORD-A4HGZXSXARIV&payment-method=boleto", payBoleto.get("redirectHref"));

        Map<String, Object> payOnlineBankDebitItau = parser.objectToMap(checkout.get("payOnlineBankDebitItau"));
        assertEquals("https://checkout-sandbox.moip.com.br/debit/itau/ORD-A4HGZXSXARIV", payOnlineBankDebitItau.get("redirectHref"));
    }

    @Play("order/list")
    @Test
    public void listOrdersTest() {

        Map<String, Object> ordersList = Moip.API.orders().list(setup);

        Map<String, Object> links = parser.objectToMap(ordersList.get("_links"));
        Map<String, Object> next = parser.objectToMap(links.get("next"));
        assertEquals("https://test.moip.com.br/v2/orders?filters=&limit=0&offset=0", next.get("href"));

        Map<String, Object> previous = parser.objectToMap(links.get("previous"));
        assertEquals("https://test.moip.com.br/v2/orders?filters=&limit=0&offset=0", previous.get("href"));

        Map<String, Object> summary = parser.objectToMap(ordersList.get("summary"));
        assertEquals(48900, summary.get("count"));
        assertEquals(7750393812L, summary.get("amount"));

        List<Map<String, Object>> orders = parser.objectToList(ordersList.get("orders"));

        // orders[0] ---
        assertEquals("ORD-PPKTLN8RH25E", orders.get(0).get("id"));
        assertNull(orders.get(0).get("ownId"));
        assertEquals("NOT_PAID", orders.get(0).get("status"));
        assertEquals(false, orders.get(0).get("blocked"));

        Map<String, Object> amount0 = parser.objectToMap(orders.get(0).get("amount"));
        assertEquals(690, amount0.get("total"));
        assertEquals(0, amount0.get("addition"));
        assertEquals(0, amount0.get("fees"));
        assertEquals(0, amount0.get("otherReceivers"));
        assertEquals("BRL", amount0.get("currency"));

        List<Map<String, Object>> receivers0 = parser.objectToList(orders.get(0).get("receivers"));
        assertEquals("PRIMARY", receivers0.get(0).get("type"));

        Map<String, Object> moipAccount0 = parser.objectToMap(receivers0.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccount0.get("id"));

        // orders[1] ---
        assertEquals("ORD-6MER91AAJI4I", orders.get(1).get("id"));
        assertNull(orders.get(1).get("ownId"));
        assertEquals("NOT_PAID", orders.get(1).get("status"));
        assertEquals(false, orders.get(1).get("blocked"));

        Map<String, Object> amount1 = parser.objectToMap(orders.get(1).get("amount"));
        assertEquals(690, amount1.get("total"));
        assertEquals(0, amount1.get("addition"));
        assertEquals(0, amount1.get("fees"));
        assertEquals(0, amount1.get("otherReceivers"));
        assertEquals("BRL", amount1.get("currency"));

        List<Map<String, Object>> receivers1 = parser.objectToList(orders.get(1).get("receivers"));
        assertEquals("PRIMARY", receivers1.get(0).get("type"));

        Map<String, Object> moipAccount1 = parser.objectToMap(receivers1.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccount1.get("id"));

        // orders[2] ---
        assertEquals("ORD-BN73SSJ1BO1K", orders.get(2).get("id"));
        assertNull(orders.get(2).get("ownId"));
        assertEquals("WAITING", orders.get(2).get("status"));
        assertEquals(false, orders.get(2).get("blocked"));

        Map<String, Object> amount2 = parser.objectToMap(orders.get(2).get("amount"));
        assertEquals(22000, amount2.get("total"));
        assertEquals(0, amount2.get("addition"));
        assertEquals(0, amount2.get("fees"));
        assertEquals(0, amount2.get("otherReceivers"));
        assertEquals("BRL", amount2.get("currency"));

        List<Map<String, Object>> receivers2 = parser.objectToList(orders.get(2).get("receivers"));
        assertEquals("PRIMARY", receivers2.get(0).get("type"));

        Map<String, Object> moipAccount2 = parser.objectToMap(receivers2.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccount2.get("id"));
    }

    @Play("order/list_order_payments")
    @Test
    public void listOrderPaymentsTest() {

        Map<String, Object> orderPayments = Moip.API.orders().listOrderPayments("ORD-BN73SSJ1BO1K", setup);
        List<Map<String, Object>> payments = parser.objectToList(orderPayments.get("payments"));
        assertEquals("PAY-174FKBMG0OFD", payments.get(0).get("id"));
        assertEquals("IN_ANALYSIS", payments.get(0).get("status"));
        assertEquals(false, payments.get(0).get("delayCapture"));

        Map<String, Object> amount = parser.objectToMap(payments.get(0).get("amount"));
        assertEquals(22000, amount.get("total"));
        assertEquals(22000, amount.get("gross"));
        assertEquals(1277, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(20723, amount.get("liquid"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(1, payments.get(0).get("installmentCount"));

        Map<String, Object> fundingInstrument = parser.objectToMap(payments.get(0).get("fundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-TH09IN444CYO", creditCard.get("id"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        Map<String, Object> holder = parser.objectToMap(creditCard.get("holder"));
        assertEquals("1986-04-26", holder.get("birthDate"));

        Map<String, Object> holderTaxDocument = parser.objectToMap(holder.get("taxDocument"));
        assertEquals("UNKNOWN", holderTaxDocument.get("type"));
        assertEquals("7847504910", holderTaxDocument.get("number"));

        assertEquals("Rafael Bosquetti Mateus", holder.get("fullname"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> acquirerDetails = parser.objectToMap(payments.get(0).get("acquirerDetails"));
        assertEquals("T12996", acquirerDetails.get("authorizationNumber"));

        Map<String, Object> taxDocument = parser.objectToMap(acquirerDetails.get("taxDocument"));
        assertEquals("CNPJ", taxDocument.get("type"));
        assertEquals("01027058000191", taxDocument.get("number"));

        List<Map<String, Object>> fees = parser.objectToList(payments.get(0).get("fees"));
        assertEquals("TRANSACTION", fees.get(0).get("type"));
        assertEquals(1277, fees.get(0).get("amount"));

        List<Map<String, Object>> events = parser.objectToList(payments.get(0).get("events"));

        // events[0] ---
        assertEquals("PAYMENT.IN_ANALYSIS", events.get(0).get("type"));
        assertEquals("2018-07-12T14:15:39.000-03", events.get(0).get("createdAt"));

        // events[1] ---
        assertEquals("PAYMENT.CREATED", events.get(1).get("type"));
        assertEquals("2018-07-12T14:15:38.000-03", events.get(1).get("createdAt"));

        Map<String, Object> links = parser.objectToMap(payments.get(0).get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-174FKBMG0OFD", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-BN73SSJ1BO1K", order.get("href"));
        assertEquals("ORD-BN73SSJ1BO1K", order.get("title"));

        assertEquals("2018-07-12T14:15:38.000-03", payments.get(0).get("createdAt"));
        assertEquals("2018-07-12T14:15:39.000-03", payments.get(0).get("updatedAt"));
    }
}
