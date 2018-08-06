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

        // orders[0]
        assertEquals("ORD-UMVGMZLNFQFW", orders.get(0).get("id"));
        assertEquals("order_1412", orders.get(0).get("ownId"));
        assertEquals("CREATED", orders.get(0).get("status"));
        assertEquals("V2", orders.get(0).get("platform"));
        assertEquals("2018-08-03T17:23:50.230-03", orders.get(0).get("createdAt"));
        assertEquals("2018-08-03T17:23:50.230-03", orders.get(0).get("updatedAt"));

        Map<String, Object> amount0 = parser.objectToMap(orders.get(0).get("amount"));
        assertEquals(0, amount0.get("paid"));
        assertEquals(4000, amount0.get("total"));
        assertEquals(0, amount0.get("fees"));
        assertEquals(0, amount0.get("refunds"));
        assertEquals(0, amount0.get("liquid"));
        assertEquals(0, amount0.get("otherReceivers"));
        assertEquals("BRL", amount0.get("currency"));

        Map<String, Object> subtotals0 = parser.objectToMap(amount0.get("subtotals"));
        assertEquals(2000, subtotals0.get("shipping"));
        assertEquals(0, subtotals0.get("addition"));
        assertEquals(0, subtotals0.get("discount"));
        assertEquals(2000, subtotals0.get("items"));

        List<Map<String, Object>> items0 = parser.objectToList(orders.get(0).get("items"));
        assertEquals("Camisa Verde e Amarelo - Brasil", items0.get(0).get("product"));
        assertEquals(2000, items0.get(0).get("price"));
        assertEquals("Seleção Brasileira", items0.get(0).get("detail"));
        assertEquals(1, items0.get(0).get("quantity"));

        Map<String, Object> customer0 = parser.objectToMap(orders.get(0).get("customer"));
        assertEquals("CUS-COEFORVOX64K", customer0.get("id"));
        assertEquals("customer[1234]", customer0.get("ownId"));
        assertEquals("Joao Sousa", customer0.get("fullname"));
        assertEquals("2015-07-01T18:12:33.000-03", customer0.get("createdAt"));
        assertEquals("2018-08-03T17:23:50.256-03", customer0.get("updatedAt"));
        assertEquals("1988-12-30", customer0.get("birthDate"));
        assertEquals("joao.sousa@email.com", customer0.get("email"));

        Map<String, Object> fundingInstrument0 = parser.objectToMap(customer0.get("fundingInstrument"));
        Map<String, Object> creditCard0 = parser.objectToMap(fundingInstrument0.get("creditCard"));
        assertEquals("CRC-A3ZNIT523OYB", creditCard0.get("id"));
        assertEquals("VISA", creditCard0.get("brand"));
        assertEquals("407302", creditCard0.get("first6"));
        assertEquals("0002", creditCard0.get("last4"));
        assertEquals(true, creditCard0.get("store"));

        assertEquals("CREDIT_CARD", fundingInstrument0.get("method"));

        Map<String, Object> phone0 = parser.objectToMap(customer0.get("phone"));
        assertEquals("55", phone0.get("countryCode"));
        assertEquals("11", phone0.get("areaCode"));
        assertEquals("66778899", phone0.get("number"));

        Map<String, Object> taxDocument0 = parser.objectToMap(customer0.get("taxDocument"));
        assertEquals("CPF", taxDocument0.get("type"));
        assertEquals("22222222222", taxDocument0.get("number"));

        Map<String, Object> shippingAddress0 = parser.objectToMap(customer0.get("shippingAddress"));
        assertEquals("01234000", shippingAddress0.get("zipCode"));
        assertEquals("Avenida Faria Lima", shippingAddress0.get("street"));
        assertEquals("2927", shippingAddress0.get("streetNumber"));
        assertEquals("8", shippingAddress0.get("complement"));
        assertEquals("Sao Paulo", shippingAddress0.get("city"));
        assertEquals("Itaim", shippingAddress0.get("district"));
        assertEquals("SP", shippingAddress0.get("state"));
        assertEquals("BRA", shippingAddress0.get("country"));

        Map<String, Object> billingAddress0 = parser.objectToMap(customer0.get("billingAddress"));
        assertEquals("01234000", billingAddress0.get("zipCode"));
        assertEquals("Avenida Faria Lima", billingAddress0.get("street"));
        assertEquals("2927", billingAddress0.get("streetNumber"));
        assertEquals("8", billingAddress0.get("complement"));
        assertEquals("Sao Paulo", billingAddress0.get("city"));
        assertEquals("Itaim", billingAddress0.get("district"));
        assertEquals("SP", billingAddress0.get("state"));
        assertEquals("BRA", billingAddress0.get("country"));

        Map<String, Object> moipAccount0 = parser.objectToMap(customer0.get("moipAccount"));
        assertEquals("MPA-PQ0H8UZYNNWY", moipAccount0.get("id"));

        Map<String, Object> customerLinks0 = parser.objectToMap(customer0.get("_links"));
        Map<String, Object> customerSelf0 = parser.objectToMap(customerLinks0.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-COEFORVOX64K", customerSelf0.get("href"));

        List<Map<String, Object>> fundingInstruments0 = parser.objectToList(customer0.get("fundingInstruments"));

        // order[0]
        // fundingInstruments0[0]
        Map<String, Object> creditCardFundInst00 = parser.objectToMap(fundingInstruments0.get(0).get("creditCard"));
        assertEquals("CRC-0DWSR8500SWI", creditCardFundInst00.get("id"));
        assertEquals("VISA", creditCardFundInst00.get("brand"));
        assertEquals("401200", creditCardFundInst00.get("first6"));
        assertEquals("3335", creditCardFundInst00.get("last4"));
        assertEquals(true, creditCardFundInst00.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments0.get(0).get("method"));

        // order[0]
        // fundingInstruments0[1]
        Map<String, Object> creditCardFundInst01 = parser.objectToMap(fundingInstruments0.get(1).get("creditCard"));
        assertEquals("CRC-PMH3KO5OPWSC", creditCardFundInst01.get("id"));
        assertEquals("VISA", creditCardFundInst01.get("brand"));
        assertEquals("401200", creditCardFundInst01.get("first6"));
        assertEquals("3335", creditCardFundInst01.get("last4"));
        assertEquals(true, creditCardFundInst01.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments0.get(1).get("method"));

        List<Map<String, Object>> payments0 = parser.objectToList(orders.get(0).get("payments"));
        assertTrue(payments0.isEmpty());

        List<Map<String, Object>> escrows0 = parser.objectToList(orders.get(0).get("escrows"));
        assertTrue(escrows0.isEmpty());

        List<Map<String, Object>> refunds0 = parser.objectToList(orders.get(0).get("refunds"));
        assertTrue(refunds0.isEmpty());

        List<Map<String, Object>> entries0 = parser.objectToList(orders.get(0).get("entries"));
        assertTrue(entries0.isEmpty());

        List<Map<String, Object>> events0 = parser.objectToList(orders.get(0).get("events"));
        assertEquals("ORDER.CREATED", events0.get(0).get("type"));
        assertEquals("2018-08-03T17:23:50.230-03", events0.get(0).get("createdAt"));
        assertEquals("", events0.get(0).get("description"));

        List<Map<String, Object>> receivers0 = parser.objectToList(orders.get(0).get("receivers"));
        Map<String, Object> receiverMoipAccount0 = parser.objectToMap(receivers0.get(0).get("moipAccount"));
        assertEquals("MPA-VB5OGTVPCI52", receiverMoipAccount0.get("id"));
        assertEquals("lojista_1@labs.moip.com.br", receiverMoipAccount0.get("login"));
        assertEquals("Chris Coyier Moip", receiverMoipAccount0.get("fullname"));

        assertEquals("PRIMARY", receivers0.get(0).get("type"));

        Map<String, Object> receiverAmount0 = parser.objectToMap(receivers0.get(0).get("amount"));
        assertEquals(4000, receiverAmount0.get("total"));
        assertEquals("BRL", receiverAmount0.get("currency"));
        assertEquals(0, receiverAmount0.get("fees"));
        assertEquals(0, receiverAmount0.get("refunds"));

        assertEquals(false, receivers0.get(0).get("feePayor"));

        Map<String, Object> links0 = parser.objectToMap(orders.get(0).get("_links"));
        Map<String, Object> self0 = parser.objectToMap(links0.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-UMVGMZLNFQFW", self0.get("href"));

        // orders[1]
        assertEquals("ORD-GRZP5CX0LSSL", orders.get(1).get("id"));
        assertEquals("order_12441512", orders.get(1).get("ownId"));
        assertEquals("CREATED", orders.get(1).get("status"));
        assertEquals("V2", orders.get(1).get("platform"));
        assertEquals("2018-08-03T17:23:50.269-03", orders.get(1).get("createdAt"));
        assertEquals("2018-08-03T17:23:50.269-03", orders.get(1).get("updatedAt"));

        Map<String, Object> amount1 = parser.objectToMap(orders.get(1).get("amount"));
        assertEquals(0, amount1.get("paid"));
        assertEquals(4000, amount1.get("total"));
        assertEquals(0, amount1.get("fees"));
        assertEquals(0, amount1.get("refunds"));
        assertEquals(0, amount1.get("liquid"));
        assertEquals(0, amount1.get("otherReceivers"));
        assertEquals("BRL", amount1.get("currency"));

        Map<String, Object> subtotals1 = parser.objectToMap(amount1.get("subtotals"));
        assertEquals(3000, subtotals1.get("shipping"));
        assertEquals(0, subtotals1.get("addition"));
        assertEquals(0, subtotals1.get("discount"));
        assertEquals(1000, subtotals1.get("items"));

        List<Map<String, Object>> items1 = parser.objectToList(orders.get(1).get("items"));
        assertEquals("Camisa Preta - Alemanha", items1.get(0).get("product"));
        assertEquals(1000, items1.get(0).get("price"));
        assertEquals("Camiseta da Copa 2014", items1.get(0).get("detail"));
        assertEquals(1, items1.get(0).get("quantity"));

        Map<String, Object> customer1 = parser.objectToMap(orders.get(1).get("customer"));
        assertEquals("CUS-COEFORVOX64K", customer1.get("id"));
        assertEquals("customer[1234]", customer1.get("ownId"));
        assertEquals("Joao Sousa", customer1.get("fullname"));
        assertEquals("2015-07-01T18:12:33.000-03", customer1.get("createdAt"));
        assertEquals("2018-08-03T17:23:50.274-03", customer1.get("updatedAt"));
        assertEquals("1988-12-30", customer1.get("birthDate"));
        assertEquals("joao.sousa@email.com", customer1.get("email"));

        Map<String, Object> fundingInstrument1 = parser.objectToMap(customer1.get("fundingInstrument"));
        Map<String, Object> creditCard1 = parser.objectToMap(fundingInstrument1.get("creditCard"));
        assertEquals("CRC-A3ZNIT523OYB", creditCard1.get("id"));
        assertEquals("VISA", creditCard1.get("brand"));
        assertEquals("407302", creditCard1.get("first6"));
        assertEquals("0002", creditCard1.get("last4"));
        assertEquals(true, creditCard1.get("store"));

        assertEquals("CREDIT_CARD", fundingInstrument1.get("method"));

        Map<String, Object> phone1 = parser.objectToMap(customer1.get("phone"));
        assertEquals("55", phone1.get("countryCode"));
        assertEquals("11", phone1.get("areaCode"));
        assertEquals("66778899", phone1.get("number"));

        Map<String, Object> taxDocument1 = parser.objectToMap(customer1.get("taxDocument"));
        assertEquals("CPF", taxDocument1.get("type"));
        assertEquals("22222222222", taxDocument1.get("number"));

        Map<String, Object> shippingAddress1 = parser.objectToMap(customer1.get("shippingAddress"));
        assertEquals("01234000", shippingAddress1.get("zipCode"));
        assertEquals("Avenida Faria Lima", shippingAddress1.get("street"));
        assertEquals("2927", shippingAddress1.get("streetNumber"));
        assertEquals("8", shippingAddress1.get("complement"));
        assertEquals("Sao Paulo", shippingAddress1.get("city"));
        assertEquals("Itaim", shippingAddress1.get("district"));
        assertEquals("SP", shippingAddress1.get("state"));
        assertEquals("BRA", shippingAddress1.get("country"));

        Map<String, Object> billingAddress1 = parser.objectToMap(customer1.get("billingAddress"));
        assertEquals("01234000", billingAddress1.get("zipCode"));
        assertEquals("Avenida Faria Lima", billingAddress1.get("street"));
        assertEquals("2927", billingAddress1.get("streetNumber"));
        assertEquals("8", billingAddress1.get("complement"));
        assertEquals("Sao Paulo", billingAddress1.get("city"));
        assertEquals("Itaim", billingAddress1.get("district"));
        assertEquals("SP", billingAddress1.get("state"));
        assertEquals("BRA", billingAddress1.get("country"));

        Map<String, Object> moipAccount1 = parser.objectToMap(customer1.get("moipAccount"));
        assertEquals("MPA-PQ0H8UZYNNWY", moipAccount1.get("id"));

        Map<String, Object> customerLinks1 = parser.objectToMap(customer1.get("_links"));
        Map<String, Object> customerSelf1 = parser.objectToMap(customerLinks1.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-COEFORVOX64K", customerSelf1.get("href"));

        List<Map<String, Object>> fundingInstruments1 = parser.objectToList(customer1.get("fundingInstruments"));

        // order[1]
        // fundingInstruments1[0]
        Map<String, Object> creditCardFundInst10 = parser.objectToMap(fundingInstruments1.get(0).get("creditCard"));
        assertEquals("CRC-0DWSR8500SWI", creditCardFundInst10.get("id"));
        assertEquals("VISA", creditCardFundInst10.get("brand"));
        assertEquals("401200", creditCardFundInst10.get("first6"));
        assertEquals("3335", creditCardFundInst10.get("last4"));
        assertEquals(true, creditCardFundInst10.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments1.get(0).get("method"));

        // order[1]
        // fundingInstruments1[1]
        Map<String, Object> creditCardFundInst11 = parser.objectToMap(fundingInstruments1.get(1).get("creditCard"));
        assertEquals("CRC-PMH3KO5OPWSC", creditCardFundInst11.get("id"));
        assertEquals("VISA", creditCardFundInst11.get("brand"));
        assertEquals("401200", creditCardFundInst11.get("first6"));
        assertEquals("3335", creditCardFundInst11.get("last4"));
        assertEquals(true, creditCardFundInst11.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments1.get(1).get("method"));

        List<Map<String, Object>> payments1 = parser.objectToList(orders.get(1).get("payments"));
        assertTrue(payments1.isEmpty());

        List<Map<String, Object>> escrows1 = parser.objectToList(orders.get(1).get("escrows"));
        assertTrue(escrows1.isEmpty());

        List<Map<String, Object>> refunds1 = parser.objectToList(orders.get(1).get("refunds"));
        assertTrue(refunds1.isEmpty());

        List<Map<String, Object>> entries1 = parser.objectToList(orders.get(1).get("entries"));
        assertTrue(entries1.isEmpty());

        List<Map<String, Object>> events1 = parser.objectToList(orders.get(1).get("events"));
        assertEquals("ORDER.CREATED", events1.get(0).get("type"));
        assertEquals("2018-08-03T17:23:50.269-03", events1.get(0).get("createdAt"));
        assertEquals("", events1.get(0).get("description"));

        List<Map<String, Object>> receivers1 = parser.objectToList(orders.get(1).get("receivers"));

        // orders[1]
        // receivers1[0]
        Map<String, Object> receiverMoipAccount10 = parser.objectToMap(receivers1.get(0).get("moipAccount"));
        assertEquals("MPA-IFYRB1HBL73Z", receiverMoipAccount10.get("id"));
        assertEquals("lojista_3@labs.moip.com.br", receiverMoipAccount10.get("login"));
        assertEquals("Lojista 3 Moip", receiverMoipAccount10.get("fullname"));

        assertEquals("PRIMARY", receivers1.get(0).get("type"));

        Map<String, Object> receiverAmount10 = parser.objectToMap(receivers1.get(0).get("amount"));
        assertEquals(3945, receiverAmount10.get("total"));
        assertEquals("BRL", receiverAmount10.get("currency"));
        assertEquals(0, receiverAmount10.get("fees"));
        assertEquals(0, receiverAmount10.get("refunds"));

        assertEquals(false, receivers1.get(0).get("feePayor"));

        // orders[1]
        // receivers1[1]
        Map<String, Object> receiverMoipAccount11 = parser.objectToMap(receivers1.get(1).get("moipAccount"));
        assertEquals("MPA-KQB1QFWS6QNM", receiverMoipAccount11.get("id"));
        assertEquals("secundario_1@labs.moip.com.br", receiverMoipAccount11.get("login"));
        assertEquals("Maria da Silva Santos", receiverMoipAccount11.get("fullname"));

        assertEquals("SECONDARY", receivers1.get(1).get("type"));

        Map<String, Object> receiverAmount11 = parser.objectToMap(receivers1.get(1).get("amount"));
        assertEquals(55, receiverAmount11.get("total"));
        assertEquals("BRL", receiverAmount11.get("currency"));
        assertEquals(0, receiverAmount11.get("fees"));
        assertEquals(0, receiverAmount11.get("refunds"));

        assertEquals(false, receivers1.get(1).get("feePayor"));

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

        // orders[0]
        assertEquals("ORD-UMVGMZLNFQFW", orders.get(0).get("id"));
        assertEquals("order_1412", orders.get(0).get("ownId"));
        assertEquals("CREATED", orders.get(0).get("status"));
        assertEquals("V2", orders.get(0).get("platform"));
        assertEquals("2018-08-03T17:23:50.000-03", orders.get(0).get("createdAt"));
        assertEquals("2018-08-03T17:23:50.000-03", orders.get(0).get("updatedAt"));

        Map<String, Object> amount0 = parser.objectToMap(orders.get(0).get("amount"));
        assertEquals(0, amount0.get("paid"));
        assertEquals(4000, amount0.get("total"));
        assertEquals(0, amount0.get("fees"));
        assertEquals(0, amount0.get("refunds"));
        assertEquals(0, amount0.get("liquid"));
        assertEquals(0, amount0.get("otherReceivers"));
        assertEquals("BRL", amount0.get("currency"));

        Map<String, Object> subtotals0 = parser.objectToMap(amount0.get("subtotals"));
        assertEquals(2000, subtotals0.get("shipping"));
        assertEquals(0, subtotals0.get("addition"));
        assertEquals(0, subtotals0.get("discount"));
        assertEquals(2000, subtotals0.get("items"));

        List<Map<String, Object>> items0 = parser.objectToList(orders.get(0).get("items"));
        assertEquals("Camisa Verde e Amarelo - Brasil", items0.get(0).get("product"));
        assertEquals(2000, items0.get(0).get("price"));
        assertEquals("Seleção Brasileira", items0.get(0).get("detail"));
        assertEquals(1, items0.get(0).get("quantity"));

        Map<String, Object> customer0 = parser.objectToMap(orders.get(0).get("customer"));
        assertEquals("CUS-COEFORVOX64K", customer0.get("id"));
        assertEquals("customer[1234]", customer0.get("ownId"));
        assertEquals("Joao Sousa", customer0.get("fullname"));
        assertEquals("2015-07-01T18:12:33.000-03", customer0.get("createdAt"));
        assertEquals("2018-08-03T17:23:50.000-03", customer0.get("updatedAt"));
        assertEquals("1988-12-30", customer0.get("birthDate"));
        assertEquals("joao.sousa@email.com", customer0.get("email"));

        Map<String, Object> fundingInstrument0 = parser.objectToMap(customer0.get("fundingInstrument"));
        Map<String, Object> creditCard0 = parser.objectToMap(fundingInstrument0.get("creditCard"));
        assertEquals("CRC-A3ZNIT523OYB", creditCard0.get("id"));
        assertEquals("VISA", creditCard0.get("brand"));
        assertEquals("407302", creditCard0.get("first6"));
        assertEquals("0002", creditCard0.get("last4"));
        assertEquals(true, creditCard0.get("store"));

        assertEquals("CREDIT_CARD", fundingInstrument0.get("method"));

        Map<String, Object> phone0 = parser.objectToMap(customer0.get("phone"));
        assertEquals("55", phone0.get("countryCode"));
        assertEquals("11", phone0.get("areaCode"));
        assertEquals("66778899", phone0.get("number"));

        Map<String, Object> taxDocument0 = parser.objectToMap(customer0.get("taxDocument"));
        assertEquals("CPF", taxDocument0.get("type"));
        assertEquals("22222222222", taxDocument0.get("number"));

        Map<String, Object> shippingAddress0 = parser.objectToMap(customer0.get("shippingAddress"));
        assertEquals("01234000", shippingAddress0.get("zipCode"));
        assertEquals("Avenida Faria Lima", shippingAddress0.get("street"));
        assertEquals("2927", shippingAddress0.get("streetNumber"));
        assertEquals("8", shippingAddress0.get("complement"));
        assertEquals("Sao Paulo", shippingAddress0.get("city"));
        assertEquals("Itaim", shippingAddress0.get("district"));
        assertEquals("SP", shippingAddress0.get("state"));
        assertEquals("BRA", shippingAddress0.get("country"));

        Map<String, Object> billingAddress0 = parser.objectToMap(customer0.get("billingAddress"));
        assertEquals("01234000", billingAddress0.get("zipCode"));
        assertEquals("Avenida Faria Lima", billingAddress0.get("street"));
        assertEquals("2927", billingAddress0.get("streetNumber"));
        assertEquals("8", billingAddress0.get("complement"));
        assertEquals("Sao Paulo", billingAddress0.get("city"));
        assertEquals("Itaim", billingAddress0.get("district"));
        assertEquals("SP", billingAddress0.get("state"));
        assertEquals("BRA", billingAddress0.get("country"));

        Map<String, Object> moipAccount0 = parser.objectToMap(customer0.get("moipAccount"));
        assertEquals("MPA-PQ0H8UZYNNWY", moipAccount0.get("id"));

        Map<String, Object> customerLinks0 = parser.objectToMap(customer0.get("_links"));
        Map<String, Object> customerSelf0 = parser.objectToMap(customerLinks0.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-COEFORVOX64K", customerSelf0.get("href"));

        List<Map<String, Object>> fundingInstruments0 = parser.objectToList(customer0.get("fundingInstruments"));

        // order[0]
        // fundingInstruments0[0]
        Map<String, Object> creditCardFundInst00 = parser.objectToMap(fundingInstruments0.get(0).get("creditCard"));
        assertEquals("CRC-0DWSR8500SWI", creditCardFundInst00.get("id"));
        assertEquals("VISA", creditCardFundInst00.get("brand"));
        assertEquals("401200", creditCardFundInst00.get("first6"));
        assertEquals("3335", creditCardFundInst00.get("last4"));
        assertEquals(true, creditCardFundInst00.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments0.get(0).get("method"));

        // order[0]
        // fundingInstruments0[1]
        Map<String, Object> creditCardFundInst01 = parser.objectToMap(fundingInstruments0.get(1).get("creditCard"));
        assertEquals("CRC-PMH3KO5OPWSC", creditCardFundInst01.get("id"));
        assertEquals("VISA", creditCardFundInst01.get("brand"));
        assertEquals("401200", creditCardFundInst01.get("first6"));
        assertEquals("3335", creditCardFundInst01.get("last4"));
        assertEquals(true, creditCardFundInst01.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments0.get(1).get("method"));

        List<Map<String, Object>> payments0 = parser.objectToList(orders.get(0).get("payments"));
        assertTrue(payments0.isEmpty());

        List<Map<String, Object>> escrows0 = parser.objectToList(orders.get(0).get("escrows"));
        assertTrue(escrows0.isEmpty());

        List<Map<String, Object>> refunds0 = parser.objectToList(orders.get(0).get("refunds"));
        assertTrue(refunds0.isEmpty());

        List<Map<String, Object>> entries0 = parser.objectToList(orders.get(0).get("entries"));
        assertTrue(entries0.isEmpty());

        List<Map<String, Object>> events0 = parser.objectToList(orders.get(0).get("events"));
        assertEquals("ORDER.CREATED", events0.get(0).get("type"));
        assertEquals("2018-08-03T17:23:50.000-03", events0.get(0).get("createdAt"));
        assertEquals("", events0.get(0).get("description"));

        List<Map<String, Object>> receivers0 = parser.objectToList(orders.get(0).get("receivers"));
        Map<String, Object> receiverMoipAccount0 = parser.objectToMap(receivers0.get(0).get("moipAccount"));
        assertEquals("MPA-VB5OGTVPCI52", receiverMoipAccount0.get("id"));
        assertEquals("lojista_1@labs.moip.com.br", receiverMoipAccount0.get("login"));
        assertEquals("Chris Coyier Moip", receiverMoipAccount0.get("fullname"));

        assertEquals("PRIMARY", receivers0.get(0).get("type"));

        Map<String, Object> receiverAmount0 = parser.objectToMap(receivers0.get(0).get("amount"));
        assertEquals(4000, receiverAmount0.get("total"));
        assertEquals("BRL", receiverAmount0.get("currency"));
        assertEquals(0, receiverAmount0.get("fees"));
        assertEquals(0, receiverAmount0.get("refunds"));

        assertEquals(false, receivers0.get(0).get("feePayor"));

        Map<String, Object> links0 = parser.objectToMap(orders.get(0).get("_links"));
        Map<String, Object> self0 = parser.objectToMap(links0.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-UMVGMZLNFQFW", self0.get("href"));

        // orders[1]
        assertEquals("ORD-GRZP5CX0LSSL", orders.get(1).get("id"));
        assertEquals("order_12441512", orders.get(1).get("ownId"));
        assertEquals("CREATED", orders.get(1).get("status"));
        assertEquals("V2", orders.get(1).get("platform"));
        assertEquals("2018-08-03T17:23:50.000-03", orders.get(1).get("createdAt"));
        assertEquals("2018-08-03T17:23:50.000-03", orders.get(1).get("updatedAt"));

        Map<String, Object> amount1 = parser.objectToMap(orders.get(1).get("amount"));
        assertEquals(0, amount1.get("paid"));
        assertEquals(4000, amount1.get("total"));
        assertEquals(0, amount1.get("fees"));
        assertEquals(0, amount1.get("refunds"));
        assertEquals(0, amount1.get("liquid"));
        assertEquals(0, amount1.get("otherReceivers"));
        assertEquals("BRL", amount1.get("currency"));

        Map<String, Object> subtotals1 = parser.objectToMap(amount1.get("subtotals"));
        assertEquals(3000, subtotals1.get("shipping"));
        assertEquals(0, subtotals1.get("addition"));
        assertEquals(0, subtotals1.get("discount"));
        assertEquals(1000, subtotals1.get("items"));

        List<Map<String, Object>> items1 = parser.objectToList(orders.get(1).get("items"));
        assertEquals("Camisa Preta - Alemanha", items1.get(0).get("product"));
        assertEquals(1000, items1.get(0).get("price"));
        assertEquals("Camiseta da Copa 2014", items1.get(0).get("detail"));
        assertEquals(1, items1.get(0).get("quantity"));

        Map<String, Object> customer1 = parser.objectToMap(orders.get(1).get("customer"));
        assertEquals("CUS-COEFORVOX64K", customer1.get("id"));
        assertEquals("customer[1234]", customer1.get("ownId"));
        assertEquals("Joao Sousa", customer1.get("fullname"));
        assertEquals("2015-07-01T18:12:33.000-03", customer1.get("createdAt"));
        assertEquals("2018-08-03T17:23:50.000-03", customer1.get("updatedAt"));
        assertEquals("1988-12-30", customer1.get("birthDate"));
        assertEquals("joao.sousa@email.com", customer1.get("email"));

        Map<String, Object> fundingInstrument1 = parser.objectToMap(customer1.get("fundingInstrument"));
        Map<String, Object> creditCard1 = parser.objectToMap(fundingInstrument1.get("creditCard"));
        assertEquals("CRC-A3ZNIT523OYB", creditCard1.get("id"));
        assertEquals("VISA", creditCard1.get("brand"));
        assertEquals("407302", creditCard1.get("first6"));
        assertEquals("0002", creditCard1.get("last4"));
        assertEquals(true, creditCard1.get("store"));

        assertEquals("CREDIT_CARD", fundingInstrument1.get("method"));

        Map<String, Object> phone1 = parser.objectToMap(customer1.get("phone"));
        assertEquals("55", phone1.get("countryCode"));
        assertEquals("11", phone1.get("areaCode"));
        assertEquals("66778899", phone1.get("number"));

        Map<String, Object> taxDocument1 = parser.objectToMap(customer1.get("taxDocument"));
        assertEquals("CPF", taxDocument1.get("type"));
        assertEquals("22222222222", taxDocument1.get("number"));

        Map<String, Object> shippingAddress1 = parser.objectToMap(customer1.get("shippingAddress"));
        assertEquals("01234000", shippingAddress1.get("zipCode"));
        assertEquals("Avenida Faria Lima", shippingAddress1.get("street"));
        assertEquals("2927", shippingAddress1.get("streetNumber"));
        assertEquals("8", shippingAddress1.get("complement"));
        assertEquals("Sao Paulo", shippingAddress1.get("city"));
        assertEquals("Itaim", shippingAddress1.get("district"));
        assertEquals("SP", shippingAddress1.get("state"));
        assertEquals("BRA", shippingAddress1.get("country"));

        Map<String, Object> billingAddress1 = parser.objectToMap(customer1.get("billingAddress"));
        assertEquals("01234000", billingAddress1.get("zipCode"));
        assertEquals("Avenida Faria Lima", billingAddress1.get("street"));
        assertEquals("2927", billingAddress1.get("streetNumber"));
        assertEquals("8", billingAddress1.get("complement"));
        assertEquals("Sao Paulo", billingAddress1.get("city"));
        assertEquals("Itaim", billingAddress1.get("district"));
        assertEquals("SP", billingAddress1.get("state"));
        assertEquals("BRA", billingAddress1.get("country"));

        Map<String, Object> moipAccount1 = parser.objectToMap(customer1.get("moipAccount"));
        assertEquals("MPA-PQ0H8UZYNNWY", moipAccount1.get("id"));

        Map<String, Object> customerLinks1 = parser.objectToMap(customer1.get("_links"));
        Map<String, Object> customerSelf1 = parser.objectToMap(customerLinks1.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-COEFORVOX64K", customerSelf1.get("href"));

        List<Map<String, Object>> fundingInstruments1 = parser.objectToList(customer1.get("fundingInstruments"));

        // order[1]
        // fundingInstruments1[0]
        Map<String, Object> creditCardFundInst10 = parser.objectToMap(fundingInstruments1.get(0).get("creditCard"));
        assertEquals("CRC-0DWSR8500SWI", creditCardFundInst10.get("id"));
        assertEquals("VISA", creditCardFundInst10.get("brand"));
        assertEquals("401200", creditCardFundInst10.get("first6"));
        assertEquals("3335", creditCardFundInst10.get("last4"));
        assertEquals(true, creditCardFundInst10.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments1.get(0).get("method"));

        // order[1]
        // fundingInstruments1[1]
        Map<String, Object> creditCardFundInst11 = parser.objectToMap(fundingInstruments1.get(1).get("creditCard"));
        assertEquals("CRC-PMH3KO5OPWSC", creditCardFundInst11.get("id"));
        assertEquals("VISA", creditCardFundInst11.get("brand"));
        assertEquals("401200", creditCardFundInst11.get("first6"));
        assertEquals("3335", creditCardFundInst11.get("last4"));
        assertEquals(true, creditCardFundInst11.get("store"));

        assertEquals("CREDIT_CARD", fundingInstruments1.get(1).get("method"));

        List<Map<String, Object>> payments1 = parser.objectToList(orders.get(1).get("payments"));
        assertTrue(payments1.isEmpty());

        List<Map<String, Object>> escrows1 = parser.objectToList(orders.get(1).get("escrows"));
        assertTrue(escrows1.isEmpty());

        List<Map<String, Object>> refunds1 = parser.objectToList(orders.get(1).get("refunds"));
        assertTrue(refunds1.isEmpty());

        List<Map<String, Object>> entries1 = parser.objectToList(orders.get(1).get("entries"));
        assertTrue(entries1.isEmpty());

        List<Map<String, Object>> events1 = parser.objectToList(orders.get(1).get("events"));
        assertEquals("ORDER.CREATED", events1.get(0).get("type"));
        assertEquals("2018-08-03T17:23:50.000-03", events1.get(0).get("createdAt"));
        assertEquals("", events1.get(0).get("description"));

        List<Map<String, Object>> receivers1 = parser.objectToList(orders.get(1).get("receivers"));

        // orders[1]
        // receivers1[0]
        Map<String, Object> receiverMoipAccount10 = parser.objectToMap(receivers1.get(0).get("moipAccount"));
        assertEquals("MPA-IFYRB1HBL73Z", receiverMoipAccount10.get("id"));
        assertEquals("lojista_3@labs.moip.com.br", receiverMoipAccount10.get("login"));
        assertEquals("Lojista 3 Moip", receiverMoipAccount10.get("fullname"));

        assertEquals("PRIMARY", receivers1.get(0).get("type"));

        Map<String, Object> receiverAmount10 = parser.objectToMap(receivers1.get(0).get("amount"));
        assertEquals(3945, receiverAmount10.get("total"));
        assertEquals("BRL", receiverAmount10.get("currency"));
        assertEquals(0, receiverAmount10.get("fees"));
        assertEquals(0, receiverAmount10.get("refunds"));

        assertEquals(false, receivers1.get(0).get("feePayor"));

        // orders[1]
        // receivers1[1]
        Map<String, Object> receiverMoipAccount11 = parser.objectToMap(receivers1.get(1).get("moipAccount"));
        assertEquals("MPA-KQB1QFWS6QNM", receiverMoipAccount11.get("id"));
        assertEquals("secundario_1@labs.moip.com.br", receiverMoipAccount11.get("login"));
        assertEquals("Maria da Silva Santos", receiverMoipAccount11.get("fullname"));

        assertEquals("SECONDARY", receivers1.get(1).get("type"));

        Map<String, Object> receiverAmount11 = parser.objectToMap(receivers1.get(1).get("amount"));
        assertEquals(55, receiverAmount11.get("total"));
        assertEquals("BRL", receiverAmount11.get("currency"));
        assertEquals(0, receiverAmount11.get("fees"));
        assertEquals(0, receiverAmount11.get("refunds"));

        assertEquals(false, receivers1.get(1).get("feePayor"));

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
}
