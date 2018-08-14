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

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BalancesTest {

    @Rule
    public Player player = new Player();
    private Parser parser = new Parser();
    private Setup setup;

    @Before
    public void initialize() {
        this.setup = new SetupFactory().setupOAuth(player.getURL("").toString());
    }

    @Play("balances/get")
    @Test
    public void getBalancesTest() {

        Map<String, Object> balances = Moip.API.balances().get(setup);

        List<Map<String, Object>> unavailable = parser.objectToList(balances.get("unavailable"));
        assertEquals(0, unavailable.get(0).get("amount"));
        assertEquals("BRL", unavailable.get(0).get("currency"));

        List<Map<String, Object>> future = parser.objectToList(balances.get("future"));
        assertEquals(64629427, future.get(0).get("amount"));
        assertEquals("BRL", future.get(0).get("currency"));

        List<Map<String, Object>> current = parser.objectToList(balances.get("current"));
        assertEquals(1094194873, current.get(0).get("amount"));
        assertEquals("BRL", current.get(0).get("currency"));
    }
}
