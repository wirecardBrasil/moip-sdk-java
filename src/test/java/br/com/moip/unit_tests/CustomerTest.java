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

public class CustomerTest {

    @Rule
    public Player player = new Player();
    private Setup setup;
    private Map<String, Object> body;
    private Parser parser;

    @Before
    public void initialize() {
        this.body = new HashMap<>();
        this.setup = new SetupFactory().setupBasicAuth(player.getURL("").toString());
        this.parser = new Parser();
    }

    @Play("customer/create")
    @Test
    public void createCustomer() {
        Map<String, Object> customer = Moip.API.customers().create(body, setup);

        assertEquals("CUS-Z83J8G3EMLIT", customer.get("id"));
        assertEquals("moip_create_customer_unit_test", customer.get("ownId"));
        assertEquals("SDK Java", customer.get("fullname"));
        assertEquals("2018-07-11T18:26:51.000-03", customer.get("createdAt"));
        assertEquals("1980-05-10", customer.get("birthDate"));
        assertEquals("sdk.java@moip.com.br", customer.get("email"));

        Map<String, Object> phone = parser.objectToMap(customer.get("phone"));
        assertEquals("55", phone.get("countryCode"));
        assertEquals("11", phone.get("areaCode"));
        assertEquals("22226842", phone.get("number"));

        Map<String, Object> taxDocument = parser.objectToMap(customer.get("taxDocument"));
        assertEquals("CPF", taxDocument.get("type"));
        assertEquals("10013390023", taxDocument.get("number"));

        Map<String, Object> shippingAddress = parser.objectToMap(customer.get("shippingAddress"));
        assertEquals("01451001", shippingAddress.get("zipCode"));
        assertEquals("Avenida Brigadeiro Faria Lima", shippingAddress.get("street"));
        assertEquals("3064", shippingAddress.get("streetNumber"));
        assertEquals("São Paulo", shippingAddress.get("city"));
        assertEquals("Itaim Bibi", shippingAddress.get("district"));
        assertEquals("SP", shippingAddress.get("state"));
        assertEquals("BRA", shippingAddress.get("country"));

        Map<String, Object> links = parser.objectToMap(customer.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-Z83J8G3EMLIT", self.get("href"));

        Map<String, Object> hostedAccount = parser.objectToMap(links.get("hostedAccount"));
        assertEquals("https://hostedaccount-sandbox.moip.com.br?token=541a137d-f94a-4ab6-9e2b-40df67309c1b&id=CUS-Z83J8G3EMLIT&mpa=MPA-CULBBYHD11", hostedAccount.get("redirectHref"));
    }

    @Play("customer/create_with_funding_instrument")
    @Test
    public void createCustomerWithFundingInstrumentTest() {

        Map<String, Object> customer = Moip.API.customers().create(body, setup);

        assertEquals("CUS-35TX8MWZ6SVX", customer.get("id").toString());
        assertEquals("moip_create_customer_unit_test", customer.get("ownId"));
        assertEquals("SDK Java", customer.get("fullname"));
        assertEquals("2018-07-11T11:57:22.000-03", customer.get("createdAt"));
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

        Map<String, Object> shippingAddress = parser.objectToMap(customer.get("shippingAddress"));
        assertEquals("01451001", shippingAddress.get("zipCode"));
        assertEquals("Avenida Brigadeiro Faria Lima", shippingAddress.get("street"));
        assertEquals("3064", shippingAddress.get("streetNumber"));
        assertEquals("São Paulo", shippingAddress.get("city"));
        assertEquals("Itaim Bibi", shippingAddress.get("district"));
        assertEquals("SP", shippingAddress.get("state"));
        assertEquals("BRA", shippingAddress.get("country"));

        Map<String, Object> links = parser.objectToMap(customer.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-35TX8MWZ6SVX", self.get("href"));

        Map<String, Object> hostedAccount = parser.objectToMap(links.get("hostedAccount"));
        assertEquals("https://hostedaccount-sandbox.moip.com.br?token=69ec54c3-c6a1-4f04-953b-af7d0f0fbee0&id=CUS-35TX8MWZ6SVX&mpa=MPA-CULBBYHD11", hostedAccount.get("redirectHref"));
    }

    @Play("customer/add_credit_card")
    @Test
    public void addCreditCardTest() {

        Map<String, Object> fundingInstrument = Moip.API.customers().addCreditCard(body, "CUS-Z83J8G3EMLIT", setup);

        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-9Y3RUBPCR30N", creditCard.get("id"));
        assertEquals("VISA", creditCard.get("brand"));
        assertEquals("401200", creditCard.get("first6"));
        assertEquals("1112", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        Map<String, Object> card = parser.objectToMap(fundingInstrument.get("card"));
        assertEquals("VISA", card.get("brand"));
        assertEquals(true, card.get("store"));

        assertEquals(false, fundingInstrument.get("isPresential"));
        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));
    }

    @Play("customer/get")
    @Test
    public void getCustomerTest() {

        Map<String, Object> customer = Moip.API.customers().get("CUS-Z83J8G3EMLIT", setup);

        assertEquals("CUS-Z83J8G3EMLIT", customer.get("id"));
        assertEquals("moip_create_customer_unit_test", customer.get("ownId"));
        assertEquals("SDK Java", customer.get("fullname"));
        assertEquals("2018-07-11T18:26:51.000-03", customer.get("createdAt"));
        assertEquals("1980-05-10", customer.get("birthDate"));
        assertEquals("sdk.java@moip.com.br", customer.get("email"));

        Map<String, Object> phone = parser.objectToMap(customer.get("phone"));
        assertEquals("55", phone.get("countryCode"));
        assertEquals("11", phone.get("areaCode"));
        assertEquals("22226842", phone.get("number"));

        Map<String, Object> taxDocument = parser.objectToMap(customer.get("taxDocument"));
        assertEquals("CPF", taxDocument.get("type"));
        assertEquals("10013390023", taxDocument.get("number"));

        Map<String, Object> shippingAddress = parser.objectToMap(customer.get("shippingAddress"));
        assertEquals("01451001", shippingAddress.get("zipCode"));
        assertEquals("Avenida Brigadeiro Faria Lima", shippingAddress.get("street"));
        assertEquals("3064", shippingAddress.get("streetNumber"));
        assertEquals("São Paulo", shippingAddress.get("city"));
        assertEquals("Itaim Bibi", shippingAddress.get("district"));
        assertEquals("SP", shippingAddress.get("state"));
        assertEquals("BRA", shippingAddress.get("country"));

        Map<String, Object> links = parser.objectToMap(customer.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-Z83J8G3EMLIT", self.get("href"));

        Map<String, Object> hostedAccount = parser.objectToMap(links.get("hostedAccount"));
        assertEquals("https://hostedaccount-sandbox.moip.com.br?token=541a137d-f94a-4ab6-9e2b-40df67309c1b&id=CUS-Z83J8G3EMLIT&mpa=MPA-CULBBYHD11", hostedAccount.get("redirectHref"));
    }

    @Play("customer/list")
    @Test
    public void listCustomersTest() {

        Map<String, Object> customersList = Moip.API.customers().list(setup);

        // Parse Object to Map List
        List<Map<String, Object>> customers = parser.objectToList(customersList.get("customers"));

        // customers[0] ---
        assertEquals("CUS-GBQJHCRKF2ZJ", customers.get(0).get("id"));

        Map<String, Object> fundingInstrument0 = parser.objectToMap(customers.get(0).get("fundingInstrument"));
        Map<String, Object> creditCard0 = parser.objectToMap(fundingInstrument0.get("creditCard"));
        assertEquals("CRC-GX8ZN1HLBHU9", creditCard0.get("id"));
        assertEquals("VISA", creditCard0.get("brand"));
        assertEquals("407302", creditCard0.get("first6"));
        assertEquals("0002", creditCard0.get("last4"));

        // customer[1] ---
        assertEquals("CUS-U9JX31DJSX6O", customers.get(1).get("id"));
        assertEquals("r@mail.co", customers.get(1).get("email"));

        Map<String, Object> fundingInstrument1 = parser.objectToMap(customers.get(1).get("fundingInstrument"));
        Map<String, Object> creditCard1 = parser.objectToMap(fundingInstrument1.get("creditCard"));
        assertEquals("CRC-QEQ0ZEC1WFCP", creditCard1.get("id"));

        Map<String, Object> shippingAddress1 = parser.objectToMap(customers.get(1).get("billingAddress"));
        assertEquals("13030-290", shippingAddress1.get("zipCode"));

        // customers[2] ---
        assertEquals("CUS-J44P8K7QLVLS", customers.get(2).get("id"));
        assertEquals("Ray Batista", customers.get(2).get("fullname"));

        Map<String, Object> fundingInstrument2 = parser.objectToMap(customers.get(2).get("fundingInstrument"));
        Map<String, Object> creditCard2 = parser.objectToMap(fundingInstrument2.get("creditCard"));
        assertEquals("CRC-VWO246R7BXKX", creditCard2.get("id"));
        assertEquals("407302", creditCard2.get("first6"));
        assertEquals(true, creditCard2.get("store"));
    }
}
