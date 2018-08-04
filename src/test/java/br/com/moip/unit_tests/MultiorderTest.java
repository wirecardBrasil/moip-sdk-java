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

public class MultiorderTest {

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

    @Play("multiorder/create")
    @Test
    public void createMultiorderTest() {

        Map<String, Object> multiorder = Moip.API.multiorders().create(body, setup);

        assertEquals("MOR-R6Q839MNWWO2", multiorder.get("id"));
        assertEquals("multiorder_id_121414", multiorder.get("ownId"));
        assertEquals("CREATED", multiorder.get("status"));
        assertEquals("2018-08-03T17:23:50.230-03", multiorder.get("createdAt"));
        assertEquals("", multiorder.get("updatedAt"));

        Map<String, Object> amount = parser.objectToMap(multiorder.get("amount"));
        assertEquals(8000, amount.get("total"));
        assertEquals("BRL", amount.get("currency"));

        List<Map<String, Object>> orders = parser.objectToList(multiorder.get("orders"));

        // orders[0]
        assertEquals("ORD-UMVGMZLNFQFW", orders.get(0).get("id"));
        assertEquals("order_1412", orders.get(0).get("ownId"));
        assertEquals("CREATED", orders.get(0).get("status"));
        assertEquals("V2", orders.get(0).get("platform"));
        assertEquals("2018-08-03T17:23:50.230-03", orders.get(0).get("createdAt"));
        assertEquals("2018-08-03T17:23:50.230-03", orders.get(0).get("updatedAt"));

        Map<String, Object> amount0 = parser.objectToMap(orders.get(0).get("amount"));
        assertEquals(0, amount0.get("paid"));
        assertEquals(4000, amount0.get("total"));
        assertEquals(0, amount0.get("fees"));
        assertEquals(0, amount0.get("refunds"));
        assertEquals(0, amount0.get("liquid"));
        assertEquals(0, amount0.get("otherReceivers"));
        assertEquals("BRL", amount0.get("currency"));

        Map<String, Object> subtotals0 = parser.objectToMap(amount0.get("subtotals"));
        assertEquals(2000, subtotals0.get("shipping"));
        assertEquals(0, subtotals0.get("addition"));
        assertEquals(0, subtotals0.get("discount"));
        assertEquals(2000, subtotals0.get("items"));

        List<Map<String, Object>> items0 = parser.objectToList(orders.get(0).get("items"));
        assertEquals("Camisa Verde e Amarelo - Brasil", items0.get(0).get("product"));
        assertEquals(2000, items0.get(0).get("price"));
        assertEquals("Seleção Brasileira", items0.get(0).get("detail"));
        assertEquals(1, items0.get(0).get("quantity"));
    }
}
