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

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EscrowTest {

    @Rule
    public Player player = new Player();
    private Parser parser = new Parser();
    private Setup setup;

    @Before
    public void initialize() {
        this.setup = new SetupFactory().setupOAuth(player.getURL("").toString());
    }

    @Play("escrow/release")
    @Test
    public void releaseEscrowTest() {

        Map<String, Object> escrow = Moip.API.escrows().release("ECW-QI13E7A7ONDQ", setup);

        assertEquals("ECW-QI13E7A7ONDQ", escrow.get("id"));
        assertEquals("RELEASED", escrow.get("status"));
        assertEquals("Custódia de pagamento", escrow.get("description"));
        assertEquals(23000, escrow.get("amount"));
        assertEquals("2018-08-20T10:27:45.000-03", escrow.get("createdAt"));
        assertEquals("2018-08-20T10:27:45.000-03", escrow.get("updatedAt"));

        Map<String, Object> links = parser.objectToMap(escrow.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/escrows/ECW-QI13E7A7ONDQ", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-FY97DC35UZ2W", order.get("href"));
        assertEquals("ORD-FY97DC35UZ2W", order.get("title"));

        Map<String, Object> payment = parser.objectToMap(links.get("payment"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-ARF0D5ELVX5D", payment.get("href"));
        assertEquals("PAY-ARF0D5ELVX5D", payment.get("title"));
    }
}
