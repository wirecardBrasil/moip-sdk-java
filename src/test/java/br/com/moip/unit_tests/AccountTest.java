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
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AccountTest {

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

    @Play("accounts/check_existence_by_tax_document")
    @Test
    public void checkExistenceByTaxDocumentTest() {

        Map<String, Object> checkExists = Moip.API.accounts().checkExistence("123.456.789-00", setup);

        assertEquals(200, checkExists.get("code"));
    }

    @Play("accounts/check_existence_by_email")
    @Test
    public void checkExistenceByEmailTest() {

        Map<String, Object> checkExists = Moip.API.accounts().checkExistence("test@moip.com.br", setup);

        assertEquals(200, checkExists.get("code"));
    }

    @Play("accounts/create_classical_account")
    @Test
    public void createClassicalAccountTest() {

        Map<String, Object> moipAccount = Moip.API.accounts().create(body, setup);

        assertEquals("MPA-6A4F2AF0CED7", moipAccount.get("id"));
        assertEquals("my_test_0012930001@moip.com.br", moipAccount.get("login"));
        assertEquals("8229ae27902746058561e2b9e71d12b9_v2", moipAccount.get("accessToken"));
        assertEquals("APP-ML6Y8VWT7XFN", moipAccount.get("channelId"));
        assertEquals("MERCHANT", moipAccount.get("type"));
        assertEquals(false, moipAccount.get("transparentAccount"));

        Map<String, Object> email = parser.objectToMap(moipAccount.get("email"));
        assertEquals("my_test_0012930001@moip.com.br", email.get("address"));
        assertEquals(false, email.get("confirmed"));

        Map<String, Object> person = parser.objectToMap(moipAccount.get("person"));
        assertEquals("Runscope", person.get("name"));
        assertEquals("Random 9123", person.get("lastName"));
        assertEquals("1990-01-01", person.get("birthDate"));

        Map<String, Object> taxDocument = parser.objectToMap(person.get("taxDocument"));
        assertEquals("CPF", taxDocument.get("type"));
        assertEquals("123.456.798-91", taxDocument.get("number"));

        Map<String, Object> address = parser.objectToMap(person.get("address"));
        assertEquals("Av. Brigadeiro Faria Lima", address.get("street"));
        assertEquals("2927", address.get("streetNumber"));
        assertEquals("Itaim", address.get("district"));
        assertEquals("São Paulo", address.get("city"));
        assertEquals("SP", address.get("state"));
        assertEquals("BRA", address.get("country"));
        assertEquals("01234000", address.get("zipCode"));

        Map<String, Object> phone = parser.objectToMap(person.get("phone"));
        assertEquals("55", phone.get("countryCode"));
        assertEquals("11", phone.get("areaCode"));
        assertEquals("965213244", phone.get("number"));
        assertEquals(false, phone.get("verified"));
        assertEquals("cellphone", phone.get("phoneType"));

        Map<String, Object> identityDocument = parser.objectToMap(person.get("identityDocument"));
        assertEquals("434322344", identityDocument.get("number"));
        assertEquals("SSP", identityDocument.get("issuer"));
        assertEquals("2000-12-12", identityDocument.get("issueDate"));
        assertEquals("RG", identityDocument.get("type"));

        Map<String, Object> company = parser.objectToMap(moipAccount.get("company"));
        assertEquals("Empresa Moip", company.get("name"));
        assertEquals("Moip Pagamentos", company.get("businessName"));

        Map<String, Object> taxDocumentCompany = parser.objectToMap(company.get("taxDocument"));
        assertEquals("CNPJ", taxDocumentCompany.get("type"));
        assertEquals("40.674.727/0001-79", taxDocumentCompany.get("number"));

        Map<String, Object> addressCompany = parser.objectToMap(company.get("address"));
        assertEquals("Av. Brigadeiro Faria Lima", addressCompany.get("street"));
        assertEquals("2927", addressCompany.get("streetNumber"));
        assertEquals("Itaim", addressCompany.get("district"));
        assertEquals("São Paulo", addressCompany.get("city"));
        assertEquals("SP", addressCompany.get("state"));
        assertEquals("BRA", addressCompany.get("country"));
        assertEquals("01234000", addressCompany.get("zipCode"));

        Map<String, Object> phoneCompany = parser.objectToMap(company.get("phone"));
        assertEquals("55", phoneCompany.get("countryCode"));
        assertEquals("11", phoneCompany.get("areaCode"));
        assertEquals("32234455", phoneCompany.get("number"));
        assertEquals(false, phoneCompany.get("verified"));
        assertEquals("not_informed", phoneCompany.get("phoneType"));

        assertEquals("2011-01-01", company.get("openingDate"));

        Map<String, Object> businessSegment = parser.objectToMap(moipAccount.get("businessSegment"));
        assertEquals(3, businessSegment.get("id"));
        assertEquals("Eletrônicos / Jogos de vídeo game", businessSegment.get("name"));
        assertEquals(7622, businessSegment.get("mcc"));

        assertEquals("2018-07-31T22:33:09.497Z", moipAccount.get("createdAt"));

        Map<String, Object> links = parser.objectToMap(moipAccount.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/moipaccounts/MPA-6A4F2AF0CED7", self.get("href"));
        assertNull(self.get("title"));

        Map<String, Object> setPassword = parser.objectToMap(links.get("setPassword"));
        assertEquals("https://desenvolvedor.moip.com.br/sandbox/AskForNewPassword.do?method=confirm&email=my_test_0012930001%40moip.com.br&code=b1cdd30fbcf728cd530f4e13c3ff4e4b", setPassword.get("href"));
    }

    @Play("accounts/create_transparent_account")
    @Test
    public void createTransparentAccountTest() {

        Map<String, Object> moipAccount = Moip.API.accounts().create(body, setup);

        assertEquals("MPA-51E0C7A09DF6", moipAccount.get("id"));
        assertEquals("my_test_1204102@moip.com.br", moipAccount.get("login"));
        assertEquals("4a2893051cfe490c8c1284d90fac8c02_v2", moipAccount.get("accessToken"));
        assertEquals("APP-ML6Y8VWT7XFN", moipAccount.get("channelId"));
        assertEquals("MERCHANT", moipAccount.get("type"));
        assertEquals(true, moipAccount.get("transparentAccount"));

        Map<String, Object> email = parser.objectToMap(moipAccount.get("email"));
        assertEquals("my_test_1204102@moip.com.br", email.get("address"));
        assertEquals(false, email.get("confirmed"));

        Map<String, Object> person = parser.objectToMap(moipAccount.get("person"));
        assertEquals("Runscope", person.get("name"));
        assertEquals("Random 9123", person.get("lastName"));
        assertEquals("1990-01-01", person.get("birthDate"));

        Map<String, Object> taxDocument = parser.objectToMap(person.get("taxDocument"));
        assertEquals("CPF", taxDocument.get("type"));
        assertEquals("123.456.798-91", taxDocument.get("number"));

        Map<String, Object> address = parser.objectToMap(person.get("address"));
        assertEquals("Av. Brigadeiro Faria Lima", address.get("street"));
        assertEquals("2927", address.get("streetNumber"));
        assertEquals("Itaim", address.get("district"));
        assertEquals("São Paulo", address.get("city"));
        assertEquals("SP", address.get("state"));
        assertEquals("BRA", address.get("country"));
        assertEquals("01234000", address.get("zipCode"));

        Map<String, Object> phone = parser.objectToMap(person.get("phone"));
        assertEquals("55", phone.get("countryCode"));
        assertEquals("11", phone.get("areaCode"));
        assertEquals("965213244", phone.get("number"));
        assertEquals(false, phone.get("verified"));
        assertEquals("cellphone", phone.get("phoneType"));

        Map<String, Object> identityDocument = parser.objectToMap(person.get("identityDocument"));
        assertEquals("434322344", identityDocument.get("number"));
        assertEquals("SSP", identityDocument.get("issuer"));
        assertEquals("2000-12-12", identityDocument.get("issueDate"));
        assertEquals("RG", identityDocument.get("type"));

        assertEquals("2018-08-01T16:26:45.903Z", moipAccount.get("createdAt"));

        Map<String, Object> links = parser.objectToMap(moipAccount.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/moipaccounts/MPA-51E0C7A09DF6", self.get("href"));
        assertNull(self.get("title"));
    }

    @Play("accounts/get")
    @Test
    public void getAccountTest() {

        Map<String, Object> moipAccount = Moip.API.accounts().get("MPA-6A4F2AF0CED7", setup);

        Map<String, Object> person = parser.objectToMap(moipAccount.get("person"));
        assertEquals("Random 9123", person.get("lastName"));

        Map<String, Object> phone = parser.objectToMap(person.get("phone"));
        assertEquals("11", phone.get("areaCode"));
        assertEquals("55", phone.get("countryCode"));
        assertEquals("965213244", phone.get("number"));

        Map<String, Object> parentsName = parser.objectToMap(person.get("parentsName"));
        assertEquals("", parentsName.get("mother"));
        assertEquals("", parentsName.get("father"));

        Map<String, Object> address = parser.objectToMap(person.get("address"));
        assertEquals("01234-000", address.get("zipCode"));
        assertEquals("Av. Brigadeiro Faria Lima", address.get("street"));
        assertEquals("SP", address.get("state"));
        assertEquals("2927", address.get("streetNumber"));
        assertEquals("Itaim", address.get("district"));
        assertEquals("BRA", address.get("country"));
        assertEquals("São Paulo", address.get("city"));

        Map<String, Object> taxDocument = parser.objectToMap(person.get("taxDocument"));
        assertEquals("123.456.798-91", taxDocument.get("number"));
        assertEquals("CPF", taxDocument.get("type"));

        assertEquals("Runscope", person.get("name"));
        assertEquals("1990-01-01", person.get("birthDate"));

        assertEquals(false, moipAccount.get("transparentAccount"));

        Map<String, Object> links = parser.objectToMap(moipAccount.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/accounts/MPA-6A4F2AF0CED7", self.get("href"));

        assertEquals(0, moipAccount.get("monthlyRevenueId"));
        assertEquals("MERCHANT", moipAccount.get("type"));
        assertEquals("MPA-6A4F2AF0CED7", moipAccount.get("id"));

        Map<String, Object> email = parser.objectToMap(moipAccount.get("email"));
        assertEquals(true, email.get("confirmed"));
        assertEquals("my_test_0012930001@moip.com.br", email.get("address"));

        assertEquals("2018-07-31T19:33:09.000-03:00", moipAccount.get("createdAt"));

        Map<String, Object> company = parser.objectToMap(moipAccount.get("company"));
        assertEquals("", company.get("monthlyRevenue"));
        assertEquals("Moip Pagamentos", company.get("businessName"));

        Map<String, Object> phoneCompany = parser.objectToMap(company.get("phone"));
        assertEquals("11", phoneCompany.get("areaCode"));
        assertEquals("55", phoneCompany.get("countryCode"));
        assertEquals("32234455", phoneCompany.get("number"));

        assertEquals("2011-01-01", company.get("openingDate"));

        Map<String, Object> addressCompany = parser.objectToMap(company.get("address"));
        assertEquals("", addressCompany.get("complement"));
        assertEquals("01234-000", addressCompany.get("zipCode"));
        assertEquals("Av. Brigadeiro Faria Lima", addressCompany.get("street"));
        assertEquals("SP", addressCompany.get("state"));
        assertEquals("2927", addressCompany.get("streetNumber"));
        assertEquals("Itaim", addressCompany.get("district"));
        assertEquals("BRA", addressCompany.get("country"));
        assertEquals("São Paulo", addressCompany.get("city"));

        Map<String, Object> taxDocumentCompany = parser.objectToMap(company.get("taxDocument"));
        assertEquals("40.674.727/0001-79", taxDocumentCompany.get("number"));
        assertEquals("CNPJ", taxDocumentCompany.get("type"));

        assertEquals("Empresa Moip", company.get("name"));
        assertEquals("", company.get("constitutionType"));

        Map<String, Object> businessSegment = parser.objectToMap(moipAccount.get("businessSegment"));
        assertEquals(3, businessSegment.get("id"));

        assertEquals("FIXED_TABLE_FEE", moipAccount.get("feeType"));
        assertEquals("my_test_0012930001@moip.com.br", moipAccount.get("login"));
        assertEquals("Empresa Moip", moipAccount.get("softDescriptor"));
    }

    @Play("accounts/get_keys")
    @Test
    public void getKeysTest() {

        Map<String, Object> getKeys = Moip.API.accounts().getKeys(setup);

        Map<String, Object> keys = parser.objectToMap(getKeys.get("keys"));
        Map<String, Object> basicAuth = parser.objectToMap(keys.get("basicAuth"));
        assertEquals("ABABABABABABABABABABABABABABABABABABABAB", basicAuth.get("secret"));
        assertEquals("01010101010101010101010101010101", basicAuth.get("token"));

        assertEquals("-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoBttaXwRoI1Fbcond5mS\n7QOb7X2lykY5hvvDeLJelvFhpeLnS4YDwkrnziM3W00UNH1yiSDU+3JhfHu5G387\nO6uN9rIHXvL+TRzkVfa5iIjG+ap2N0/toPzy5ekpgxBicjtyPHEgoU6dRzdszEF4\nItimGk5ACx/lMOvctncS5j3uWBaTPwyn0hshmtDwClf6dEZgQvm/dNaIkxHKV+9j\nMn3ZfK/liT8A3xwaVvRzzuxf09xJTXrAd9v5VQbeWGxwFcW05oJulSFjmJA9Hcmb\nDYHJT+sG2mlZDEruCGAzCVubJwGY1aRlcs9AQc1jIm/l8JwH7le2kpk3QoX+gz0w\nWwIDAQAB\n-----END PUBLIC KEY-----\n", keys.get("encryption"));
    }
}
