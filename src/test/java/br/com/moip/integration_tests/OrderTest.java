package br.com.moip.integration_tests;

import br.com.moip.Moip;
import br.com.moip.auth.Authentication;
import br.com.moip.auth.BasicAuth;
import br.com.moip.exception.UnexpectedException;
import br.com.moip.exception.ValidationException;
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

    @Test
    public void createOrderTest() {

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
        product1.put("price", 1000000);

        Map<String, Object> product2 = new HashMap<>();
        product2.put("product", "Product 2 Description");
        product2.put("category", "SCIENCE_AND_LABORATORY");
        product2.put("quantity", 5);
        product2.put("detail", "Pym particles");
        product2.put("price", 24500000);

        List<Map<String, Object>> items = new ArrayList<>();
        items.add(product1);
        items.add(product2);

        Map<String, Object> customer = new HashMap<>();
        customer.put("id", "CUS-XXOBPZ80QLYP");

        Map<String, Object> orderBody = new HashMap<>();
        orderBody.put("ownId", "asagasfg121asd31as");
        orderBody.put("amount", amount);
        orderBody.put("items", items);
        orderBody.put("customer", customer);

        try {

            Map<String, Object> order = Moip.API.orders().create(orderBody, setup);

            System.out.println("create: " + order);

        } catch (ValidationException e) {
            System.out.println("Validation!");
            System.out.println(e.getErrors().getErrors());
            System.out.println(e.getMessage());
            e.getStackTrace();
        } catch (UnexpectedException e) {
            System.out.println("Unexpected!");
            System.out.println(e);
            e.getStackTrace();
        }
    }

    @Test
    public void getOrderTest() {

        try {

            Map<String, Object> responseGet = Moip.API.orders().get("ORD-LS7CZHW90N3Y", setup);

            System.out.println("get: " + responseGet);

        } catch (ValidationException e) {
            System.out.println("Validation!");
            System.out.println(e.getErrors().getErrors());
            System.out.println(e.getMessage());
            e.getStackTrace();
        } catch (UnexpectedException e) {
            System.out.println("Unexpected!");
            System.out.println(e);
            e.getStackTrace();
        }
    }

    @Test
    public void listOrdersTest() {

        try {

            Map<String, Object> responseList = Moip.API.orders().list(setup);

            System.out.println("list: " + responseList);

        } catch (ValidationException e) {
            System.out.println("Validation!");
            System.out.println(e.getErrors().getErrors());
            System.out.println(e.getMessage());
            e.getStackTrace();
        } catch (UnexpectedException e) {
            System.out.println("Unexpected!");
            System.out.println(e);
            e.getStackTrace();
        }
    }

    @Test
    public void listOrderPayments() {

        try {

            Map<String, Object> responseListOrderPayments = Moip.API.orders().listOrderPayments("ORD-S36MOUQ97AP6", setup);

            System.out.println("list of order payments: " + responseListOrderPayments);

        } catch (ValidationException e) {
            System.out.println("Validation!");
            System.out.println(e.getErrors().getErrors());
            System.out.println(e.getMessage());
            e.getStackTrace();
        } catch (UnexpectedException e) {
            System.out.println("Unexpected!");
            System.out.println(e);
            e.getStackTrace();
        }
    }
}
