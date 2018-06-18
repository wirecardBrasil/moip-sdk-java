package br.com.moip.integration_tests;

import br.com.moip.auth.Authentication;
import br.com.moip.auth.BasicAuth;
import br.com.moip.models.Order;
import br.com.moip.models.Setup;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderTest {

    private final static String token = "01010101010101010101010101010101";
    private final static String key = "ABABABABABABABABABABABABABABABABABABABAB";

    private static Authentication auth = new BasicAuth(token, key);

    private Setup setup = new Setup().setAuthentication(auth).setEnvironment(Setup.Environment.SANDBOX);

    private Order order = new Order();

    private Map<String, Object> customerRequestBody = new HashMap<>();

    private Map<String, Object> taxDocument = new HashMap<>();

    private Map<String, Object> phone = new HashMap<>();

    private Map<String, Object> shippingAddress = new HashMap<>();

    @Test
    public void test() {
        Map<String, Object> subtotals = new HashMap<>();
        subtotals.put("shipping", 15000);

        Map<String, Object> amount = new HashMap<>();
        amount.put("currency", "BRL");
        amount.put("subtotals", subtotals);

        Map<String, Object> product1 = new HashMap<>();
        product1.put("product", "Product Description");
        product1.put("category", "CLOTHING");
        product1.put("quantity", 2);
        product1.put("detail", "Anakin's Light Saber");
        product1.put("price", 100000000);

        List items = new ArrayList();
        items.add(product1);

        Map<String, Object> customer = new HashMap<>();
        customer.put("id", "CUS-XXOBPZ80QLYP");

        Map<String, Object> orderBody = new HashMap<>();
        orderBody.put("ownId", "asfasaggas");
        orderBody.put("amount", amount);
        orderBody.put("items", items);
        orderBody.put("customer", customer);

        Map<String, Object> responseCreation = order.create(orderBody, setup);

        System.out.println(responseCreation);

        String id = responseCreation.get("id").toString();

        Map<String, Object> responseGet = order.get(id, setup);

        System.out.println(responseGet);
    }
}
