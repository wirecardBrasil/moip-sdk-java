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

public class TransferTest {

    @Rule
    public Player player = new Player();
    private Parser parser = new Parser();
    private Setup setup;
    private Map<String, Object> body;
    private Map<String, Object> variables;

    @Before
    public void initialize() {
        this.body = new HashMap<>();
        this.variables = new HashMap<>();
        this.setup = new SetupFactory().setupOAuth(player.getURL("").toString());
    }

    @Play("transfer/create")
    @Test
    public void createTransferTest() {

        variables.put("fee", 0);
        variables.put("amount", 500);
        variables.put("updatedAt", "2018-08-17T17:45:15.804Z");
        variables.put("id", "TRA-EW1UQ14495JI");
        variables.put("moipAccountId", "MPA-92DC9F7EF8B5");
        variables.put("moipAccountEmail", "wemersonsantoos@hotmail.com");
        variables.put("moipAccountLogin", "wemersonsantoos@hotmail.com");
        variables.put("moipAccountFullname", "Wemerson Santos");
        variables.put("senderAccountId", "MPA-CULBBYHD11");
        variables.put("transferInstrumentMethod", "MOIP_ACCOUNT");
        variables.put("status", "REQUESTED");
        variables.put("eventCreatedAt", "2018-08-17T17:45:16.000Z");
        variables.put("eventDescription", "Requested");
        variables.put("eventType", "TRANSFER.REQUESTED");
        variables.put("createdAt", "2018-08-17T17:45:15.804Z");
        variables.put("role", "PAYER");
        variables.put("selfHref", "https://sandbox.moip.com.br/v2/transfers/TRA-EW1UQ14495JI");

        Map<String, Object> transfer = Moip.API.transfers().create(body, setup);

        testTransferBody(transfer, variables);
    }

    @Play("transfer/revert")
    @Test
    public void revertTransferTest() {

        variables.put("fee", 0);
        variables.put("amount", 500);
        variables.put("updatedAt", "2018-08-17T17:45:52.372Z");
        variables.put("id", "TRA-EW1UQ14495JI");
        variables.put("moipAccountId", "MPA-92DC9F7EF8B5");
        variables.put("moipAccountEmail", "wemersonsantoos@hotmail.com");
        variables.put("moipAccountLogin", "wemersonsantoos@hotmail.com");
        variables.put("moipAccountFullname", "Wemerson Santos");
        variables.put("senderAccountId", "MPA-CULBBYHD11");
        variables.put("transferInstrumentMethod", "MOIP_ACCOUNT");
        variables.put("status", "REVERSED");
        variables.put("eventCreatedAt", "2018-08-17T17:45:16.000Z");
        variables.put("eventDescription", "Requested");
        variables.put("eventType", "TRANSFER.REQUESTED");
        variables.put("eventCreatedAt2", "2018-08-17T17:45:52.000Z");
        variables.put("eventDescription2", "Reversed");
        variables.put("eventType2", "TRANSFER.REVERSED");
        variables.put("createdAt", "2018-08-17T17:45:16.000Z");
        variables.put("role", "RECEIVER");
        variables.put("reverseHref", "https://sandbox.moip.com.br/v2/transfers/TRA-EW1UQ14495JI/reverse");
        variables.put("selfHref", "https://sandbox.moip.com.br/v2/transfers/TRA-EW1UQ14495JI");

        Map<String, Object> revert = Moip.API.transfers().revert("TRA-EW1UQ14495JI", setup);

        testTransferBody(revert, variables);
    }

    @Play("transfer/get")
    @Test
    public void getTransferTest() {

        variables.put("fee", 0);
        variables.put("amount", 500);
        variables.put("updatedAt", "2018-08-17T17:45:52.000Z");
        variables.put("id", "TRA-EW1UQ14495JI");
        variables.put("moipAccountId", "MPA-92DC9F7EF8B5");
        variables.put("moipAccountEmail", "wemersonsantoos@hotmail.com");
        variables.put("moipAccountLogin", "wemersonsantoos@hotmail.com");
        variables.put("moipAccountFullname", "Wemerson Santos");
        variables.put("senderAccountId", "MPA-CULBBYHD11");
        variables.put("transferInstrumentMethod", "MOIP_ACCOUNT");
        variables.put("status", "REVERSED");
        variables.put("eventCreatedAt", "2018-08-17T17:45:16.000Z");
        variables.put("eventDescription", "Requested");
        variables.put("eventType", "TRANSFER.REQUESTED");
        variables.put("eventCreatedAt2", "2018-08-17T17:45:52.000Z");
        variables.put("eventDescription2", "Reversed");
        variables.put("eventType2", "TRANSFER.REVERSED");
        variables.put("createdAt", "2018-08-17T17:45:16.000Z");
        variables.put("role", "PAYER");
        variables.put("selfHref", "https://sandbox.moip.com.br/v2/transfers/TRA-EW1UQ14495JI");

        Map<String, Object> transfer = Moip.API.transfers().get("TRA-EW1UQ14495JI", setup);

        testTransferBody(transfer, variables);
    }

    @Play("transfer/list")
    @Test
    public void listTransfersTest() {

        variables.put("fee", 0);
        variables.put("amount", 500);
        variables.put("updatedAt", "2018-08-17T17:45:52.000Z");
        variables.put("id", "TRA-EW1UQ14495JI");
        variables.put("moipAccountId", "MPA-92DC9F7EF8B5");
        variables.put("moipAccountEmail", "wemersonsantoos@hotmail.com");
        variables.put("moipAccountLogin", "wemersonsantoos@hotmail.com");
        variables.put("moipAccountFullname", "Wemerson Santos");
        variables.put("senderAccountId", "MPA-CULBBYHD11");
        variables.put("transferInstrumentMethod", "MOIP_ACCOUNT");
        variables.put("status", "REVERSED");
        variables.put("eventCreatedAt", "2018-08-17T17:45:16.000Z");
        variables.put("eventDescription", "Requested");
        variables.put("eventType", "TRANSFER.REQUESTED");
        variables.put("eventCreatedAt2", "2018-08-17T17:45:52.000Z");
        variables.put("eventDescription2", "Reversed");
        variables.put("eventType2", "TRANSFER.REVERSED");
        variables.put("createdAt", "2018-08-17T17:45:16.000Z");
        variables.put("role", "PAYER");
        variables.put("selfHref", "https://sandbox.moip.com.br/v2/transfers/TRA-EW1UQ14495JI");

        Map<String, Object> list = Moip.API.transfers().list(setup);

        Map<String, Object> summary = parser.objectToMap(list.get("summary"));
        assertEquals(659741662, summary.get("amount"));
        assertEquals(31776, summary.get("count"));

        List<Map<String, Object>> transfers = parser.objectToList(list.get("transfers"));
        testTransferBody(transfers.get(0), variables);

        Map<String, Object> links = parser.objectToMap(list.get("_links"));
        Map<String, Object> previous = parser.objectToMap(links.get("previous"));
        assertEquals("https://sandbox.moip.com.br/v2/transfers?offset=0&limit=20", previous.get("href"));

        Map<String, Object> next = parser.objectToMap(links.get("next"));
        assertEquals("https://sandbox.moip.com.br/v2/transfers?offset=20&limit=20", next.get("href"));
    }

    private void testTransferBody(Map<String, Object> transfer, Map<String, Object> variables) {

        assertEquals(variables.get("fee"), transfer.get("fee"));
        assertEquals(variables.get("amount"), transfer.get("amount"));
        assertEquals(variables.get("updatedAt"), transfer.get("updatedAt"));
        assertEquals(variables.get("id"), transfer.get("id"));

        Map<String, Object> transferInstrument = parser.objectToMap(transfer.get("transferInstrument"));
        testTransferInstrument(transferInstrument, variables);

        assertEquals(variables.get("status"), transfer.get("status"));

        if (transfer.get("events") != null) {

            List<Map<String, Object>> events = parser.objectToList(transfer.get("events"));
            testEvents(events.get(0), variables);
        }

        assertEquals(variables.get("createdAt"), transfer.get("createdAt"));
        assertEquals(variables.get("role"), transfer.get("role"));

        Map<String, Object> links = parser.objectToMap(transfer.get("_links"));
        testLinks(links, variables);
    }

    private void testTransferInstrument(Map<String, Object> transferInstrument, Map<String, Object> variables) {

        Map<String, Object> moipAccount = parser.objectToMap(transferInstrument.get("moipAccount"));
        assertEquals(variables.get("moipAccountId"), moipAccount.get("id"));
        assertEquals(variables.get("moipAccountEmail"), moipAccount.get("email"));
        assertEquals(variables.get("moipAccountLogin"), moipAccount.get("login"));
        assertEquals(variables.get("moipAccountFullname"), moipAccount.get("fullname"));

        assertEquals(variables.get("senderAccountId"), transferInstrument.get("senderAccountId"));
        assertEquals(variables.get("transferInstrumentMethod"), transferInstrument.get("method"));
    }

    private void testEvents(Map<String, Object> event, Map<String, Object> variables) {

        assertEquals(variables.get("eventCreatedAt"), event.get("createdAt"));
        assertEquals(variables.get("eventDescription"), event.get("description"));
        assertEquals(variables.get("eventType"), event.get("type"));

        if (event.get("eventCreatedAt2") != null) {
            assertEquals(variables.get("eventCreatedAt2"), event.get("createdAt"));
            assertEquals(variables.get("eventDescription2"), event.get("description"));
            assertEquals(variables.get("eventType2"), event.get("type"));
        }
    }

    private void testLinks(Map<String, Object> links, Map<String, Object> variables) {

        if (links.get("reverse") != null) {

            Map<String, Object> reverse = parser.objectToMap(links.get("reverse"));
            assertEquals(variables.get("reverseHref"), reverse.get("href"));
        }

        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals(variables.get("selfHref"), self.get("href"));
    }
}
