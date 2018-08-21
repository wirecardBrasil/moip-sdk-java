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

public class BankAccountTest {

    @Rule
    public Player player = new Player();
    private Setup setup;
    private Map<String, Object> body;
    private Parser parser = new Parser();
    private Map<String, Object> variables;

    @Before
    public void initialize() {
        this.body = new HashMap<>();
        this.setup = new SetupFactory().setupOAuth(player.getURL("").toString());
        this.variables = new HashMap<>();
    }

    @Play("bankaccount/get")
    @Test
    public void getBankAccountTest() {

        variables.put("id", "BKA-EQW51MWMAO22");
        variables.put("agencyNumber", "1111");
        variables.put("taxDocumentNumber", "255.328.259-12");
        variables.put("taxDocumentType", "CPF");
        variables.put("holderThirdParty", false);
        variables.put("holderFullname", "Jose Silva dos Santos");
        variables.put("accountNumber", "11111111");
        variables.put("status", "IN_VERIFICATION");
        variables.put("createdAt", "2016-04-07T15:23:48.000-03:00");
        variables.put("accountCheckNumber", "1");
        variables.put("selfHref", "https://sandbox.moip.com.br//accounts/BKA-EQW51MWMAO22/bankaccounts");
        variables.put("bankName", "BANCO DO BRASIL S.A.");
        variables.put("type", "CHECKING");
        variables.put("agencyCheckNumber", "1");
        variables.put("bankNumber", "001");

        Map<String, Object> bankAccount = Moip.API.bankAccounts().get("BKA-EQW51MWMAO22", setup);

        testBankAccountBody(bankAccount, variables);
    }

    @Play("bankaccount/update")
    @Test
    public void updateBankAccountTest() {

        variables.put("id", "BKA-EQW51MWMAO22");
        variables.put("agencyNumber", "2222");
        variables.put("taxDocumentNumber", "255.328.259-12");
        variables.put("taxDocumentType", "CPF");
        variables.put("holderThirdParty", false);
        variables.put("holderFullname", "Jose Silva dos Santos");
        variables.put("accountNumber", "12365478");
        variables.put("status", "VERIFIED");
        variables.put("createdAt", "2016-04-07T15:23:48.000-03:00");
        variables.put("accountCheckNumber", "1");
        variables.put("selfHref", "https://sandbox.moip.com.br//accounts/BKA-EQW51MWMAO22/bankaccounts");
        variables.put("bankName", "BANCO DO BRASIL S.A.");
        variables.put("type", "CHECKING");
        variables.put("agencyCheckNumber", "1");
        variables.put("bankNumber", "001");

        Map<String, Object> bankAccount = Moip.API.bankAccounts().update(body, "BKA-EQW51MWMAO22", setup);
        testBankAccountBody(bankAccount, variables);
    }

    void testBankAccountBody(Map<String, Object> bankAccount, Map<String, Object> variables) {

        assertEquals(variables.get("id"), bankAccount.get("id"));
        assertEquals(variables.get("agencyNumber"), bankAccount.get("agencyNumber"));

        Map<String, Object> holder = parser.objectToMap(bankAccount.get("holder"));
        testBankAccountHolder(holder, variables);

        assertEquals(variables.get("accountNumber"), bankAccount.get("accountNumber"));
        assertEquals(variables.get("status"), bankAccount.get("status"));
        assertEquals(variables.get("createdAt"), bankAccount.get("createdAt"));
        assertEquals(variables.get("accountCheckNumber"), bankAccount.get("accountCheckNumber"));

        Map<String, Object> links = parser.objectToMap(bankAccount.get("_links"));
        testBankAccountLinks(links, variables);

        assertEquals(variables.get("bankName"), bankAccount.get("bankName"));
        assertEquals(variables.get("type"), bankAccount.get("type"));
        assertEquals(variables.get("agencyCheckNumber"), bankAccount.get("agencyCheckNumber"));
        assertEquals(variables.get("bankNumber"), bankAccount.get("bankNumber"));
    }

    private void testBankAccountHolder(Map<String, Object> holder, Map<String, Object> variables) {

        Map<String, Object> taxDocument = parser.objectToMap(holder.get("taxDocument"));
        assertEquals(variables.get("taxDocumentNumber"), taxDocument.get("number"));
        assertEquals(variables.get("taxDocumentType"), taxDocument.get("type"));

        assertEquals(variables.get("holderThirdParty"), holder.get("thirdParty"));
        assertEquals(variables.get("holderFullname"), holder.get("fullname"));
    }

    private void testBankAccountLinks(Map<String, Object> links, Map<String, Object> variables) {

        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals(variables.get("selfHref"), self.get("href"));
    }
}
