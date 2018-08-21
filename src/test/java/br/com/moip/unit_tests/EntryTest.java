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

public class EntryTest {

    @Rule
    public Player player = new Player();
    private Parser parser = new Parser();
    private Map<String, Object> variables;
    private Setup setup;

    @Before
    public void initialize() {
        this.setup = new SetupFactory().setupOAuth(player.getURL("").toString());
        this.variables = new HashMap<>();
    }


    @Play("entry/get")
    @Test
    public void getEntryTest() {

        variables.put("scheduledFor", "2012-08-31T16:59:36.000Z");
        variables.put("status", "SETTLED");
        variables.put("moipAccountId", "MPA-CULBBYHD11");
        variables.put("selfTitle", "ENT-2JHP5A593QSW");
        variables.put("selfHref", "https://sandbox.moip.com.br/v2/entries/ENT-2JHP5A593QSW");
        variables.put("feesAmount", 150);
        variables.put("feesType", "TRANSACTION");
        variables.put("type", "CREDIT_CARD");
        variables.put("ownId", "");
        variables.put("updatedAt", "2014-04-16T16:16:12.000Z");
        variables.put("id", "ENT-2JHP5A593QSW");
        variables.put("amountFee", 150);
        variables.put("amountTotal", 150);
        variables.put("amountLiquid", 0);
        variables.put("amountCurrency", "BRL");
        variables.put("operation", "CREDIT");
        variables.put("createdAt", "2014-04-16T16:16:12.000Z");
        variables.put("event", "PAY-AQITTDNDKBU9");
        variables.put("description", "Cartao de credito - Pedido ORD-UF4E00XMFDL1");
        variables.put("occurrenceTo", 1);
        variables.put("occurrenceIn", 1);
        variables.put("blocked", false);
        variables.put("settledAt", "2012-08-31T00:00:00.000Z");
        variables.put("additionalId", 145470);

        Map<String, Object> entry = Moip.API.entries().get("ENT-2JHP5A593QSW", setup);
        testEntryBody(entry, variables);
    }

    @Play("entry/list")
    @Test
    public void listEntriesTest() {

        variables.put("scheduledFor", "2012-08-31T16:59:36.000Z");
        variables.put("status", "SETTLED");
        variables.put("selfTitle", "ENT-2JHP5A593QSW");
        variables.put("selfHref", "https://sandbox.moip.com.br/v2/entries/ENT-2JHP5A593QSW");
        variables.put("feesAmount", 150);
        variables.put("feesType", "TRANSACTION");
        variables.put("type", "CREDIT_CARD");
        variables.put("ownId", "");
        variables.put("updatedAt", "2014-04-16T16:16:12.000Z");
        variables.put("id", "ENT-2JHP5A593QSW");
        variables.put("amountFee", 150);
        variables.put("amountTotal", 150);
        variables.put("amountLiquid", 0);
        variables.put("amountCurrency", "BRL");
        variables.put("operation", "CREDIT");
        variables.put("createdAt", "2014-04-16T16:16:12.000Z");
        variables.put("event", "PAY-AQITTDNDKBU9");
        variables.put("description", "Cartao de credito - Pedido ORD-UF4E00XMFDL1");
        variables.put("occurrenceTo", 1);
        variables.put("occurrenceIn", 1);
        variables.put("blocked", false);
        variables.put("settledAt", "2012-08-31T00:00:00.000Z");
        variables.put("additionalId", 145470);

        Map<String, Object> list = Moip.API.entries().list(setup);

        Map<String, Object> summary = parser.objectToMap(list.get("summary"));
        assertEquals(1494494396, summary.get("amount"));
        assertEquals(48561, summary.get("count"));

        Map<String, Object> links = parser.objectToMap(list.get("_links"));
        Map<String, Object> previous = parser.objectToMap(links.get("previous"));
        assertEquals("https://sandbox.moip.com.br/v2/entries?offset=0&limit=20", previous.get("href"));

        Map<String, Object> next = parser.objectToMap(links.get("next"));
        assertEquals("https://sandbox.moip.com.br/v2/entries?offset=20&limit=20", next.get("href"));

        List<Map<String, Object>> entries = parser.objectToList(list.get("entries"));
        testEntryBody(entries.get(0), variables);
    }

    private void testEntryBody(Map<String, Object> entry, Map<String, Object> variables) {

        assertEquals(variables.get("scheduledFor"), entry.get("scheduledFor"));
        assertEquals(variables.get("status"), entry.get("status"));

        if (entry.get("moipAccount") != null) {
            Map<String, Object> moipAccount = parser.objectToMap(entry.get("moipAccount"));
            assertEquals(variables.get("moipAccountId"), moipAccount.get("id"));
        }

        Map<String, Object> links = parser.objectToMap(entry.get("_links"));
        testLinks(links, variables);

        if (entry.get("fees") != null) {
            List<Map<String, Object>> fees = parser.objectToList(entry.get("fees"));
            assertEquals(variables.get("feesAmount"), fees.get(0).get("amount"));
            assertEquals(variables.get("feesType"), fees.get(0).get("type"));
        }

        assertEquals(variables.get("type"), entry.get("type"));
        assertEquals(variables.get("ownId"), entry.get("ownId"));
        assertEquals(variables.get("updatedAt"), entry.get("updatedAt"));
        assertEquals(variables.get("id"), entry.get("id"));

        Map<String, Object> amount = parser.objectToMap(entry.get("amount"));
        testAmount(amount, variables);

        assertEquals(variables.get("operation"), entry.get("operation"));
        assertEquals(variables.get("createdAt"), entry.get("createdAt"));
        assertEquals(variables.get("event"), entry.get("event"));
        assertEquals(variables.get("description"), entry.get("description"));

        Map<String, Object> occurrence = parser.objectToMap(entry.get("occurrence"));
        assertEquals(variables.get("occurrenceTo"), occurrence.get("to"));
        assertEquals(variables.get("occurrenceIn"), occurrence.get("in"));

        assertEquals(variables.get("blocked"), entry.get("blocked"));
        assertEquals(variables.get("settledAt"), entry.get("settledAt"));
        assertEquals(variables.get("additionalId"), entry.get("additionalId"));
    }

    private void testLinks(Map<String, Object> links, Map<String, Object> variables) {

        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals(variables.get("selfTitle"), self.get("title"));
        assertEquals(variables.get("selfHref"), self.get("href"));
    }

    private void testAmount(Map<String, Object> amount, Map<String, Object> variables) {

        assertEquals(variables.get("amountFee"), amount.get("fee"));
        assertEquals(variables.get("amountTotal"), amount.get("total"));
        assertEquals(variables.get("amountLiquid"), amount.get("liquid"));
        assertEquals(variables.get("amountCurrency"), amount.get("currency"));
    }
}
