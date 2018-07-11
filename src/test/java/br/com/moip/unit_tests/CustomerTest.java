package br.com.moip.unit_tests;

import br.com.moip.Moip;
import br.com.moip.models.Setup;
import br.com.moip.unit_tests.setup.SetupFactory;
import br.com.moip.util.ObjectToMap;
import com.rodrigosaito.mockwebserver.player.Play;
import com.rodrigosaito.mockwebserver.player.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CustomerTest {

    @Rule
    public Player player = new Player();
    private Setup setup;
    private Map<String, Object> body;
    private ObjectToMap converter;

    @Before
    public void initialize() {
        this.body = new HashMap<>();
        this.setup = new SetupFactory().setup(player.getURL("").toString());
        this.converter = new ObjectToMap();
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

        Map<String, Object> phone = converter.convert(customer.get("phone"));
        assertEquals("55", phone.get("countryCode"));
        assertEquals("11", phone.get("areaCode"));
        assertEquals("22226842", phone.get("number"));

        Map<String, Object> taxDocument = converter.convert(customer.get("taxDocument"));
        assertEquals("CPF", taxDocument.get("type"));
        assertEquals("10013390023", taxDocument.get("number"));

        Map<String, Object> shippingAddress = converter.convert(customer.get("shippingAddress"));
        assertEquals("01451001", shippingAddress.get("zipCode"));
        assertEquals("Avenida Brigadeiro Faria Lima", shippingAddress.get("street"));
        assertEquals("3064", shippingAddress.get("streetNumber"));
        assertEquals("São Paulo", shippingAddress.get("city"));
        assertEquals("Itaim Bibi", shippingAddress.get("district"));
        assertEquals("SP", shippingAddress.get("state"));
        assertEquals("BRA", shippingAddress.get("country"));

        Map<String, Object> links = converter.convert(customer.get("_links"));
        Map<String, Object> self = converter.convert(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-Z83J8G3EMLIT", self.get("href"));

        Map<String, Object> hostedAccount = converter.convert(links.get("hostedAccount"));
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

        Map<String, Object> fundingInstrument = converter.convert(customer.get("fundingInstrument"));
        Map<String, Object> creditCard = converter.convert(fundingInstrument.get("creditCard"));
        assertEquals("CRC-7GKFB3S2R8SS", creditCard.get("id"));
        assertEquals("ELO", creditCard.get("brand"));
        assertEquals("636297", creditCard.get("first6"));
        assertEquals("7013", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> phone = converter.convert(customer.get("phone"));
        assertEquals("55", phone.get("countryCode"));
        assertEquals("11", phone.get("areaCode"));
        assertEquals("22226842", phone.get("number"));

        Map<String, Object> taxDocument = converter.convert(customer.get("taxDocument"));
        assertEquals("CPF", taxDocument.get("type"));
        assertEquals("10013390023", taxDocument.get("number"));

        Map<String, Object> shippingAddress = converter.convert(customer.get("shippingAddress"));
        assertEquals("01451001", shippingAddress.get("zipCode"));
        assertEquals("Avenida Brigadeiro Faria Lima", shippingAddress.get("street"));
        assertEquals("3064", shippingAddress.get("streetNumber"));
        assertEquals("São Paulo", shippingAddress.get("city"));
        assertEquals("Itaim Bibi", shippingAddress.get("district"));
        assertEquals("SP", shippingAddress.get("state"));
        assertEquals("BRA", shippingAddress.get("country"));

        Map<String, Object> links = converter.convert(customer.get("_links"));
        Map<String, Object> self = converter.convert(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/customers/CUS-35TX8MWZ6SVX", self.get("href"));

        Map<String, Object> hostedAccount = converter.convert(links.get("hostedAccount"));
        assertEquals("https://hostedaccount-sandbox.moip.com.br?token=69ec54c3-c6a1-4f04-953b-af7d0f0fbee0&id=CUS-35TX8MWZ6SVX&mpa=MPA-CULBBYHD11", hostedAccount.get("redirectHref"));
    }

    @Play("customer/add_credit_card")
    @Test
    public void addCreditCardTest() {

        Map<String, Object> fundingInstrument = Moip.API.customers().addCreditCard(body, "CUS-Z83J8G3EMLIT", setup);

        Map<String, Object> creditCard = converter.convert(fundingInstrument.get("creditCard"));
        assertEquals("CRC-9Y3RUBPCR30N", creditCard.get("id"));
        assertEquals("VISA", creditCard.get("brand"));
        assertEquals("401200", creditCard.get("first6"));
        assertEquals("1112", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        Map<String, Object> card = converter.convert(fundingInstrument.get("card"));
        assertEquals("VISA", card.get("brand"));
        assertEquals(true, card.get("store"));

        assertEquals(false, fundingInstrument.get("isPresential"));
        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));
    }
}
