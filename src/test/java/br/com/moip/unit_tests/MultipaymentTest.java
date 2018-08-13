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

public class MultipaymentTest {

    @Rule
    public Player player = new Player();
    private Parser parser = new Parser();
    private Setup setup;
    private Map<String, Object> variables;

    @Before
    public void initialize() {
        this.variables = new HashMap<>();
        this.setup = new SetupFactory().setupOAuth(player.getURL("").toString());
    }

    @Play("multipayment/capture_pre_authorized")
    @Test
    public void capturePreAuthorizedTest() {

        variables.put("id", "PAY-8JYH1UX882LL");
        variables.put("status", "AUTHORIZED");
        variables.put("delayCapture", true);

        Map<String, Object> multipayment = Moip.API.multipayments().capturePreAuthorized("MPY-EVAWT7HR8QEP", setup);

        assertEquals("MPY-EVAWT7HR8QEP", multipayment.get("id"));
        assertEquals("AUTHORIZED", multipayment.get("status"));

        Map<String, Object> amount = parser.objectToMap(multipayment.get("amount"));
        assertEquals(8000, amount.get("total"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(1, multipayment.get("installmentCount"));

        List<Map<String, Object>> payments = parser.objectToList(multipayment.get("payments"));
        testPaymentsFromMultipayment(payments.get(0), variables);

        Map<String, Object> links = parser.objectToMap(multipayment.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/multipayments/MPY-EVAWT7HR8QEP", self.get("href"));

        Map<String, Object> multiorder = parser.objectToMap(links.get("multiorder"));
        assertEquals("https://sandbox.moip.com.br/v2/multiorders/MOR-ZB4W3W0S0NUF", multiorder.get("href"));
    }

    @Play("multipayment/cancel_pre_authorized")
    @Test
    public void cancelPreAuthorizedTest() {

        variables.put("id", "PAY-TEXWW7CUBHM1");
        variables.put("status", "CANCELLED");
        variables.put("delayCapture", true);

        Map<String, Object> multipayment = Moip.API.multipayments().cancelPreAuthorized("MPY-8X6E4HB9FNGK", setup);

        assertEquals("MPY-8X6E4HB9FNGK", multipayment.get("id"));
        assertEquals("CANCELLED", multipayment.get("status"));

        Map<String, Object> amount = parser.objectToMap(multipayment.get("amount"));
        assertEquals(8000, amount.get("total"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(1, multipayment.get("installmentCount"));

        List<Map<String, Object>> payments = parser.objectToList(multipayment.get("payments"));
        testPaymentsFromMultipayment(payments.get(0), variables);

        Map<String, Object> links = parser.objectToMap(multipayment.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/multipayments/MPY-8X6E4HB9FNGK", self.get("href"));

        Map<String, Object> multiorder = parser.objectToMap(links.get("multiorder"));
        assertEquals("https://sandbox.moip.com.br/v2/multiorders/MOR-2A6SGDFHQ54O", multiorder.get("href"));
    }

    @Play("multipayment/get")
    @Test
    public void getMultipaymentTest() {

        variables.put("id", "PAY-DY1WE4PCPMLZ");
        variables.put("status", "AUTHORIZED");
        variables.put("delayCapture", false);

        Map<String, Object> multipayment = Moip.API.multipayments().get("MPY-TTNVH6J6KBNC", setup);

        assertEquals("MPY-TTNVH6J6KBNC", multipayment.get("id"));
        assertEquals("AUTHORIZED", multipayment.get("status"));

        Map<String, Object> amount = parser.objectToMap(multipayment.get("amount"));
        assertEquals(8000, amount.get("total"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(1, multipayment.get("installmentCount"));

        List<Map<String, Object>> payments = parser.objectToList(multipayment.get("payments"));
        testPaymentsFromMultipayment(payments.get(0), variables);

        Map<String, Object> links = parser.objectToMap(multipayment.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/multipayments/MPY-TTNVH6J6KBNC", self.get("href"));

        Map<String, Object> multiorder = parser.objectToMap(links.get("multiorder"));
        assertEquals("https://sandbox.moip.com.br/v2/multiorders/MOR-R6Q839MNWWO2", multiorder.get("href"));
    }

    void testPaymentsFromMultipayment(Map<String, Object> payment, Map<String, Object> variables) {

        assertEquals(variables.get("id"), payment.get("id"));
        assertEquals(variables.get("status"), payment.get("status"));
        assertEquals(variables.get("delayCapture"), payment.get("delayCapture"));

        if ("CANCELLED".equals(variables.get("status"))) {
            Map<String, Object> cancellationDetails = parser.objectToMap(payment.get("cancellationDetails"));
            assertEquals("7", cancellationDetails.get("code"));
            assertEquals("Política do Moip", cancellationDetails.get("description"));
            assertEquals("MOIP", cancellationDetails.get("cancelledBy"));
        }

        Map<String, Object> amount = parser.objectToMap(payment.get("amount"));
        assertEquals(4000, amount.get("total"));
        assertEquals(4000, amount.get("gross"));
        assertEquals(289, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(3711, amount.get("liquid"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(1, payment.get("installmentCount"));

        Map<String, Object> fundingInstrument = parser.objectToMap(payment.get("fundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-2HY3YCJS9WPD", creditCard.get("id"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        Map<String, Object> holder = parser.objectToMap(creditCard.get("holder"));
        assertEquals("1988-12-30", holder.get("birthDate"));

        Map<String, Object> taxDocument = parser.objectToMap(holder.get("taxDocument"));
        assertEquals("CPF", taxDocument.get("type"));
        assertEquals("33333333333", taxDocument.get("number"));

        assertEquals("Jose Portador da Silva", holder.get("fullname"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> acquirerDetails = parser.objectToMap(payment.get("acquirerDetails"));

        if (acquirerDetails.get("authorizationNumber") != null)
            assertEquals("123456", acquirerDetails.get("authorizationNumber"));

        Map<String, Object> document = parser.objectToMap(acquirerDetails.get("taxDocument"));
        assertEquals("CNPJ", document.get("type"));
        assertEquals("01027058000191", document.get("number"));

        List<Map<String, Object>> fees = parser.objectToList(payment.get("fees"));
        assertEquals("TRANSACTION", fees.get(0).get("type"));
        assertEquals(289, fees.get(0).get("amount"));

        List<Map<String, Object>> receivers = parser.objectToList(payment.get("receivers"));
        testReceivers(receivers.get(0));

        if (1 < receivers.size()) testReceivers(receivers.get(1));
    }

    private void testReceivers(Map<String, Object> receiver) {

        Map<String, Object> moipAccount = parser.objectToMap(receiver.get("moipAccount"));
        assertEquals("MPA-VB5OGTVPCI52", moipAccount.get("id"));
        assertEquals("lojista_1@labs.moip.com.br", moipAccount.get("login"));
        assertEquals("Chris Coyier Moip", moipAccount.get("fullname"));

        Map<String, Object> amount = parser.objectToMap(receiver.get("amount"));
        assertEquals(4000, amount.get("total"));
        assertEquals("BRL", amount.get("currency"));
        assertEquals(289, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
    }
}
