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
import static org.junit.Assert.assertTrue;

public class MultiorderTest {

    @Rule
    public Player player = new Player();
    private Setup setup;
    private Map<String, Object> body;
    private Parser parser;

    @Before
    public void initialize() {
        this.body = new HashMap<>();
        this.setup = new SetupFactory().setupOAuth(player.getURL("").toString());
        this.parser = new Parser();
    }

    @Play("multiorder/create")
    @Test
    public void createMultiorderTest() {

        Map<String, Object> multiorder = Moip.API.multiorders().create(body, setup);

        assertEquals("MOR-R6Q839MNWWO2", multiorder.get("id"));
        assertEquals("multiorder_id_121414", multiorder.get("ownId"));
        assertEquals("CREATED", multiorder.get("status"));
        assertEquals("2018-08-03T17:23:50.230-03", multiorder.get("createdAt"));
        assertEquals("", multiorder.get("updatedAt"));

        Map<String, Object> amount = parser.objectToMap(multiorder.get("amount"));
        assertEquals(8000, amount.get("total"));
        assertEquals("BRL", amount.get("currency"));

        List<Map<String, Object>> orders = parser.objectToList(multiorder.get("orders"));
        firstOrderFromCreate(orders.get(0));
        secondOrderFromCreate(orders.get(1));

        Map<String, Object> links = parser.objectToMap(multiorder.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/MOR-R6Q839MNWWO2", self.get("href"));

        Map<String, Object> checkout = parser.objectToMap(links.get("checkout"));
        Map<String, Object> payCreditCard = parser.objectToMap(checkout.get("payCreditCard"));
        assertEquals("https://checkout-sandbox.moip.com.br/creditcard/MOR-R6Q839MNWWO2", payCreditCard.get("redirectHref"));

        Map<String, Object> payBoleto = parser.objectToMap(checkout.get("payBoleto"));
        assertEquals("https://checkout-sandbox.moip.com.br/boleto/MOR-R6Q839MNWWO2", payBoleto.get("redirectHref"));

        Map<String, Object> payOnlineBankDebitItau = parser.objectToMap(checkout.get("payOnlineBankDebitItau"));
        assertEquals("https://checkout-sandbox.moip.com.br/debit/itau/MOR-R6Q839MNWWO2", payOnlineBankDebitItau.get("redirectHref"));
    }

    @Play("multiorder/get")
    @Test
    public void getMultiorderTest() {

        Map<String, Object> multiorder = Moip.API.multiorders().get("MOR-R6Q839MNWWO2", setup);

        assertEquals("MOR-R6Q839MNWWO2", multiorder.get("id"));
        assertEquals("multiorder_id_121414", multiorder.get("ownId"));
        assertEquals("CREATED", multiorder.get("status"));
        assertEquals("2018-08-03T17:23:50.000-03", multiorder.get("createdAt"));
        assertEquals("", multiorder.get("updatedAt"));

        Map<String, Object> amount = parser.objectToMap(multiorder.get("amount"));
        assertEquals(8000, amount.get("total"));
        assertEquals("BRL", amount.get("currency"));

        List<Map<String, Object>> orders = parser.objectToList(multiorder.get("orders"));
        firstOrderFromGet(orders.get(0));
        secondOrderFromGet(orders.get(1));

        Map<String, Object> links1 = parser.objectToMap(orders.get(1).get("_links"));
        Map<String, Object> self1 = parser.objectToMap(links1.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-GRZP5CX0LSSL", self1.get("href"));

        Map<String, Object> links = parser.objectToMap(multiorder.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/MOR-R6Q839MNWWO2", self.get("href"));

        Map<String, Object> checkout = parser.objectToMap(links.get("checkout"));
        Map<String, Object> payCreditCard = parser.objectToMap(checkout.get("payCreditCard"));
        assertEquals("https://checkout-sandbox.moip.com.br/creditcard/MOR-R6Q839MNWWO2", payCreditCard.get("redirectHref"));

        Map<String, Object> payBoleto = parser.objectToMap(checkout.get("payBoleto"));
        assertEquals("https://checkout-sandbox.moip.com.br/boleto/MOR-R6Q839MNWWO2", payBoleto.get("redirectHref"));

        Map<String, Object> payOnlineBankDebitItau = parser.objectToMap(checkout.get("payOnlineBankDebitItau"));
        assertEquals("https://checkout-sandbox.moip.com.br/debit/itau/MOR-R6Q839MNWWO2", payOnlineBankDebitItau.get("redirectHref"));
    }

    private void customer(Map<String, Object> customer) {

        assertEquals("CUS-COEFORVOX64K", customer.get("id"));
        assertEquals("customer[1234]", customer.get("ownId"));
        assertEquals("Joao Sousa", customer.get("fullname"));
        assertEquals("1988-12-30", customer.get("birthDate"));
        assertEquals("joao.sousa@email.com", customer.get("email"));

        Map<String, Object> fundingInstrument = parser.objectToMap(customer.get("fundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-A3ZNIT523OYB", creditCard.get("id"));
        assertEquals("VISA", creditCard.get("brand"));
        assertEquals("407302", creditCard.get("first6"));
        assertEquals("0002", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> phone = parser.objectToMap(customer.get("phone"));
        assertEquals("55", phone.get("countryCode"));
        assertEquals("11", phone.get("areaCode"));
        assertEquals("66778899", phone.get("number"));

        Map<String, Object> taxDocument = parser.objectToMap(customer.get("taxDocument"));
        assertEquals("CPF", taxDocument.get("type"));
        assertEquals("22222222222", taxDocument.get("number"));

        Map<String, Object> shippingAddress = parser.objectToMap(customer.get("shippingAddress"));
        assertEquals("01234000", shippingAddress.get("zipCode"));
        assertEquals("Avenida Faria Lima", shippingAddress.get("street"));
        assertEquals("2927", shippingAddress.get("streetNumber"));
        assertEquals("8", shippingAddress.get("complement"));
        assertEquals("Sao Paulo", shippingAddress.get("city"));
        assertEquals("Itaim", shippingAddress.get("district"));
        assertEquals("SP", shippingAddress.get("state"));
        assertEquals("BRA", shippingAddress.get("country"));

        Map<String, Object> billingAddress = parser.objectToMap(customer.get("billingAddress"));
        assertEquals("01234000", billingAddress.get("zipCode"));
        assertEquals("Avenida Faria Lima", billingAddress.get("street"));
        assertEquals("2927", billingAddress.get("streetNumber"));
        assertEquals("8", billingAddress.get("complement"));
        assertEquals("Sao Paulo", billingAddress.get("city"));
        assertEquals("Itaim", billingAddress.get("district"));
        assertEquals("SP", billingAddress.get("state"));
        assertEquals("BRA", billingAddress.get("country"));

        Map<String, Object> moipAccount = parser.objectToMap(customer.get("moipAccount"));
        assertEquals("MPA-PQ0H8UZYNNWY", moipAccount.get("id"));

        Map<String, Object> customerLinks = parser.objectToMap(customer.get("_links"));
        Map<String, Object> customerSelf = parser.objectToMap(customerLinks.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-COEFORVOX64K", customerSelf.get("href"));

        List<Map<String, Object>> fundingInstruments = parser.objectToList(customer.get("fundingInstruments"));

        Map<String, Object> firstCreditCard = parser.objectToMap(fundingInstruments.get(0).get("creditCard"));
        assertEquals("CRC-0DWSR8500SWI", firstCreditCard.get("id"));
        assertEquals("VISA", firstCreditCard.get("brand"));
        assertEquals("401200", firstCreditCard.get("first6"));
        assertEquals("3335", firstCreditCard.get("last4"));
        assertEquals(true, firstCreditCard.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments.get(0).get("method"));

        Map<String, Object> secondCreditCard = parser.objectToMap(fundingInstruments.get(1).get("creditCard"));
        assertEquals("CRC-PMH3KO5OPWSC", secondCreditCard.get("id"));
        assertEquals("VISA", secondCreditCard.get("brand"));
        assertEquals("401200", secondCreditCard.get("first6"));
        assertEquals("3335", secondCreditCard.get("last4"));
        assertEquals(true, secondCreditCard.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments.get(1).get("method"));
    }

    private void firstOrderFromCreate(Map<String, Object> order) {

        assertEquals("ORD-UMVGMZLNFQFW", order.get("id"));
        assertEquals("order_1412", order.get("ownId"));
        assertEquals("CREATED", order.get("status"));
        assertEquals("V2", order.get("platform"));
        assertEquals("2018-08-03T17:23:50.230-03", order.get("createdAt"));
        assertEquals("2018-08-03T17:23:50.230-03", order.get("updatedAt"));

        Map<String, Object> amount = parser.objectToMap(order.get("amount"));
        assertEquals(0, amount.get("paid"));
        assertEquals(4000, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(0, amount.get("liquid"));
        assertEquals(0, amount.get("otherReceivers"));
        assertEquals("BRL", amount.get("currency"));

        Map<String, Object> subtotals = parser.objectToMap(amount.get("subtotals"));
        assertEquals(2000, subtotals.get("shipping"));
        assertEquals(0, subtotals.get("addition"));
        assertEquals(0, subtotals.get("discount"));
        assertEquals(2000, subtotals.get("items"));

        List<Map<String, Object>> items = parser.objectToList(order.get("items"));
        assertEquals("Camisa Verde e Amarelo - Brasil", items.get(0).get("product"));
        assertEquals(2000, items.get(0).get("price"));
        assertEquals("Seleção Brasileira", items.get(0).get("detail"));
        assertEquals(1, items.get(0).get("quantity"));

        Map<String, Object> customer = parser.objectToMap(order.get("customer"));
        customer(customer);

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
        assertEquals("2018-08-03T17:23:50.230-03", events.get(0).get("createdAt"));
        assertEquals("", events.get(0).get("description"));

        List<Map<String, Object>> receivers = parser.objectToList(order.get("receivers"));
        Map<String, Object> receiverMoipAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-VB5OGTVPCI52", receiverMoipAccount.get("id"));
        assertEquals("lojista_1@labs.moip.com.br", receiverMoipAccount.get("login"));
        assertEquals("Chris Coyier Moip", receiverMoipAccount.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> receiverAmount = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(4000, receiverAmount.get("total"));
        assertEquals("BRL", receiverAmount.get("currency"));
        assertEquals(0, receiverAmount.get("fees"));
        assertEquals(0, receiverAmount.get("refunds"));

        assertEquals(false, receivers.get(0).get("feePayor"));

        Map<String, Object> links0 = parser.objectToMap(order.get("_links"));
        Map<String, Object> self0 = parser.objectToMap(links0.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-UMVGMZLNFQFW", self0.get("href"));
    }

    private void secondOrderFromCreate(Map<String, Object> order) {

        assertEquals("ORD-GRZP5CX0LSSL", order.get("id"));
        assertEquals("order_12441512", order.get("ownId"));
        assertEquals("CREATED", order.get("status"));
        assertEquals("V2", order.get("platform"));
        assertEquals("2018-08-03T17:23:50.269-03", order.get("createdAt"));
        assertEquals("2018-08-03T17:23:50.269-03", order.get("updatedAt"));

        Map<String, Object> amount = parser.objectToMap(order.get("amount"));
        assertEquals(0, amount.get("paid"));
        assertEquals(4000, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(0, amount.get("liquid"));
        assertEquals(0, amount.get("otherReceivers"));
        assertEquals("BRL", amount.get("currency"));

        Map<String, Object> subtotals = parser.objectToMap(amount.get("subtotals"));
        assertEquals(3000, subtotals.get("shipping"));
        assertEquals(0, subtotals.get("addition"));
        assertEquals(0, subtotals.get("discount"));
        assertEquals(1000, subtotals.get("items"));

        List<Map<String, Object>> items = parser.objectToList(order.get("items"));
        assertEquals("Camisa Preta - Alemanha", items.get(0).get("product"));
        assertEquals(1000, items.get(0).get("price"));
        assertEquals("Camiseta da Copa 2014", items.get(0).get("detail"));
        assertEquals(1, items.get(0).get("quantity"));

        Map<String, Object> customer = parser.objectToMap(order.get("customer"));
        customer(customer);

        List<Map<String, Object>> payments1 = parser.objectToList(order.get("payments"));
        assertTrue(payments1.isEmpty());

        List<Map<String, Object>> escrows1 = parser.objectToList(order.get("escrows"));
        assertTrue(escrows1.isEmpty());

        List<Map<String, Object>> refunds1 = parser.objectToList(order.get("refunds"));
        assertTrue(refunds1.isEmpty());

        List<Map<String, Object>> entries1 = parser.objectToList(order.get("entries"));
        assertTrue(entries1.isEmpty());

        List<Map<String, Object>> events1 = parser.objectToList(order.get("events"));
        assertEquals("ORDER.CREATED", events1.get(0).get("type"));
        assertEquals("2018-08-03T17:23:50.269-03", events1.get(0).get("createdAt"));
        assertEquals("", events1.get(0).get("description"));

        List<Map<String, Object>> receivers = parser.objectToList(order.get("receivers"));

        Map<String, Object> firstReceiverAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-IFYRB1HBL73Z", firstReceiverAccount.get("id"));
        assertEquals("lojista_3@labs.moip.com.br", firstReceiverAccount.get("login"));
        assertEquals("Lojista 3 Moip", firstReceiverAccount.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> firstReceiverAmount = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(3945, firstReceiverAmount.get("total"));
        assertEquals("BRL", firstReceiverAmount.get("currency"));
        assertEquals(0, firstReceiverAmount.get("fees"));
        assertEquals(0, firstReceiverAmount.get("refunds"));

        assertEquals(false, receivers.get(0).get("feePayor"));

        Map<String, Object> secondReceiverAccount = parser.objectToMap(receivers.get(1).get("moipAccount"));
        assertEquals("MPA-KQB1QFWS6QNM", secondReceiverAccount.get("id"));
        assertEquals("secundario_1@labs.moip.com.br", secondReceiverAccount.get("login"));
        assertEquals("Maria da Silva Santos", secondReceiverAccount.get("fullname"));

        assertEquals("SECONDARY", receivers.get(1).get("type"));

        Map<String, Object> secondReceiverAmount = parser.objectToMap(receivers.get(1).get("amount"));
        assertEquals(55, secondReceiverAmount.get("total"));
        assertEquals("BRL", secondReceiverAmount.get("currency"));
        assertEquals(0, secondReceiverAmount.get("fees"));
        assertEquals(0, secondReceiverAmount.get("refunds"));

        assertEquals(false, receivers.get(1).get("feePayor"));

        Map<String, Object> links1 = parser.objectToMap(order.get("_links"));
        Map<String, Object> self1 = parser.objectToMap(links1.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-GRZP5CX0LSSL", self1.get("href"));
    }

    private void firstOrderFromGet(Map<String, Object> order) {

        assertEquals("ORD-UMVGMZLNFQFW", order.get("id"));
        assertEquals("order_1412", order.get("ownId"));
        assertEquals("CREATED", order.get("status"));
        assertEquals("V2", order.get("platform"));
        assertEquals("2018-08-03T17:23:50.000-03", order.get("createdAt"));
        assertEquals("2018-08-03T17:23:50.000-03", order.get("updatedAt"));

        Map<String, Object> amount = parser.objectToMap(order.get("amount"));
        assertEquals(0, amount.get("paid"));
        assertEquals(4000, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(0, amount.get("liquid"));
        assertEquals(0, amount.get("otherReceivers"));
        assertEquals("BRL", amount.get("currency"));

        Map<String, Object> subtotals = parser.objectToMap(amount.get("subtotals"));
        assertEquals(2000, subtotals.get("shipping"));
        assertEquals(0, subtotals.get("addition"));
        assertEquals(0, subtotals.get("discount"));
        assertEquals(2000, subtotals.get("items"));

        List<Map<String, Object>> items = parser.objectToList(order.get("items"));
        assertEquals("Camisa Verde e Amarelo - Brasil", items.get(0).get("product"));
        assertEquals(2000, items.get(0).get("price"));
        assertEquals("Seleção Brasileira", items.get(0).get("detail"));
        assertEquals(1, items.get(0).get("quantity"));

        Map<String, Object> customer = parser.objectToMap(order.get("customer"));
        customer(customer);

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
        assertEquals("2018-08-03T17:23:50.000-03", events.get(0).get("createdAt"));
        assertEquals("", events.get(0).get("description"));

        List<Map<String, Object>> receivers = parser.objectToList(order.get("receivers"));
        Map<String, Object> receiverMoipAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-VB5OGTVPCI52", receiverMoipAccount.get("id"));
        assertEquals("lojista_1@labs.moip.com.br", receiverMoipAccount.get("login"));
        assertEquals("Chris Coyier Moip", receiverMoipAccount.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> receiverAmount = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(4000, receiverAmount.get("total"));
        assertEquals("BRL", receiverAmount.get("currency"));
        assertEquals(0, receiverAmount.get("fees"));
        assertEquals(0, receiverAmount.get("refunds"));

        assertEquals(false, receivers.get(0).get("feePayor"));

        Map<String, Object> links0 = parser.objectToMap(order.get("_links"));
        Map<String, Object> self0 = parser.objectToMap(links0.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-UMVGMZLNFQFW", self0.get("href"));
    }

    private void secondOrderFromGet(Map<String, Object> order) {

        assertEquals("ORD-GRZP5CX0LSSL", order.get("id"));
        assertEquals("order_12441512", order.get("ownId"));
        assertEquals("CREATED", order.get("status"));
        assertEquals("V2", order.get("platform"));
        assertEquals("2018-08-03T17:23:50.000-03", order.get("createdAt"));
        assertEquals("2018-08-03T17:23:50.000-03", order.get("updatedAt"));

        Map<String, Object> amount = parser.objectToMap(order.get("amount"));
        assertEquals(0, amount.get("paid"));
        assertEquals(4000, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(0, amount.get("liquid"));
        assertEquals(0, amount.get("otherReceivers"));
        assertEquals("BRL", amount.get("currency"));

        Map<String, Object> subtotals = parser.objectToMap(amount.get("subtotals"));
        assertEquals(3000, subtotals.get("shipping"));
        assertEquals(0, subtotals.get("addition"));
        assertEquals(0, subtotals.get("discount"));
        assertEquals(1000, subtotals.get("items"));

        List<Map<String, Object>> items = parser.objectToList(order.get("items"));
        assertEquals("Camisa Preta - Alemanha", items.get(0).get("product"));
        assertEquals(1000, items.get(0).get("price"));
        assertEquals("Camiseta da Copa 2014", items.get(0).get("detail"));
        assertEquals(1, items.get(0).get("quantity"));

        Map<String, Object> customer = parser.objectToMap(order.get("customer"));
        customer(customer);

        List<Map<String, Object>> payments1 = parser.objectToList(order.get("payments"));
        assertTrue(payments1.isEmpty());

        List<Map<String, Object>> escrows1 = parser.objectToList(order.get("escrows"));
        assertTrue(escrows1.isEmpty());

        List<Map<String, Object>> refunds1 = parser.objectToList(order.get("refunds"));
        assertTrue(refunds1.isEmpty());

        List<Map<String, Object>> entries1 = parser.objectToList(order.get("entries"));
        assertTrue(entries1.isEmpty());

        List<Map<String, Object>> events1 = parser.objectToList(order.get("events"));
        assertEquals("ORDER.CREATED", events1.get(0).get("type"));
        assertEquals("2018-08-03T17:23:50.000-03", events1.get(0).get("createdAt"));
        assertEquals("", events1.get(0).get("description"));

        List<Map<String, Object>> receivers = parser.objectToList(order.get("receivers"));

        Map<String, Object> firstReceiverAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-IFYRB1HBL73Z", firstReceiverAccount.get("id"));
        assertEquals("lojista_3@labs.moip.com.br", firstReceiverAccount.get("login"));
        assertEquals("Lojista 3 Moip", firstReceiverAccount.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> firstReceiverAmount = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(3945, firstReceiverAmount.get("total"));
        assertEquals("BRL", firstReceiverAmount.get("currency"));
        assertEquals(0, firstReceiverAmount.get("fees"));
        assertEquals(0, firstReceiverAmount.get("refunds"));

        assertEquals(false, receivers.get(0).get("feePayor"));

        Map<String, Object> secondReceiverAccount = parser.objectToMap(receivers.get(1).get("moipAccount"));
        assertEquals("MPA-KQB1QFWS6QNM", secondReceiverAccount.get("id"));
        assertEquals("secundario_1@labs.moip.com.br", secondReceiverAccount.get("login"));
        assertEquals("Maria da Silva Santos", secondReceiverAccount.get("fullname"));

        assertEquals("SECONDARY", receivers.get(1).get("type"));

        Map<String, Object> secondReceiverAmount = parser.objectToMap(receivers.get(1).get("amount"));
        assertEquals(55, secondReceiverAmount.get("total"));
        assertEquals("BRL", secondReceiverAmount.get("currency"));
        assertEquals(0, secondReceiverAmount.get("fees"));
        assertEquals(0, secondReceiverAmount.get("refunds"));

        assertEquals(false, receivers.get(1).get("feePayor"));

        Map<String, Object> links1 = parser.objectToMap(order.get("_links"));
        Map<String, Object> self1 = parser.objectToMap(links1.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-GRZP5CX0LSSL", self1.get("href"));
    }
}
