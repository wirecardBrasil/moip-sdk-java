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

    @Play("account/check_existence_by_tax_document")
    @Test
    public void checkExistenceByTaxDocumentTest() {

        Map<String, Object> checkExists = Moip.API.account().checkExistence("123.456.789-00", setup);

        assertEquals(200, checkExists.get("code"));
    }

    @Play("account/check_existence_by_email")
    @Test
    public void checkExistenceByEmailTest() {

        Map<String, Object> checkExists = Moip.API.account().checkExistence("test@moip.com.br", setup);

        assertEquals(200, checkExists.get("code"));
    }

    @Play("account/create_classical_account")
    @Test
    public void createClassicalAccountTest() {

        Map<String, Object> moipAccount = Moip.API.account().create(body, setup);

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
}
