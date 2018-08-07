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

public class MultipaymentTest {

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

    @Play("multipayment/capture_pre_authorized")
    @Test
    public void capturePreAuthorizedTest() {

        Map<String, Object> multipayment = Moip.API.multipayments().capturePreAuthorized("MPY-R6Q839MNWWO2", setup);
    }

    @Play("multipayment/cancel_pre_authorized")
    @Test
    public void cancelPreAuthorizedTest() {

        Map<String, Object> multipayment = Moip.API.multipayments().cancelPreAuthorized("MPY-EVAWT7HR8QEP", setup);
    }

    @Play("multipayment/get")
    @Test
    public void getMultipaymentTest() {

        Map<String, Object> multipayment = Moip.API.multipayments().get("MPY-TTNVH6J6KBNC", setup);
    }

    private void testPaymentsFromMultipayment(Map<String, Object> payment, Map<String, Object> variables) {

        assertEquals(variables.get("id"), payment.get("id"));
        assertEquals(variables.get("status"), payment.get("status"));
        assertEquals(variables.get("delayCapture"), payment.get("delayCapture"));

        if (variables.get("status") == "CANCELLED") {
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
        assertEquals("123456", acquirerDetails.get("authorizationNumber"));

        Map<String, Object> document = parser.objectToMap(acquirerDetails.get("taxDocument"));
        assertEquals("CNPJ", document.get("type"));
        assertEquals("01027058000191", document.get("number"));
    }
}
