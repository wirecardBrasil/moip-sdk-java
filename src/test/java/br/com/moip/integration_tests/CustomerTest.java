package br.com.moip.integration_tests;

import br.com.moip.auth.Authentication;
import br.com.moip.auth.BasicAuth;
import br.com.moip.models.Customer;
import br.com.moip.models.Setup;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CustomerTest {

    private final static String token = "01010101010101010101010101010101";
    private final static String key = "ABABABABABABABABABABABABABABABABABABABAB";

    private static Authentication auth = new BasicAuth(token, key);

    private Setup setup = new Setup().setAuthentication(auth).setEnvironment(Setup.Environment.SANDBOX);

    private Customer customer = new Customer();

    private Map<String, Object> customerRequestBody = new HashMap<>();

    private Map<String, Object> taxDocument = new HashMap<>();

    private Map<String, Object> phone = new HashMap<>();

    private Map<String, Object> shippingAddress = new HashMap<>();

    private Map<String, Object> createCustomer() {

        taxDocument.put("type", "CPF");
        taxDocument.put("number", "10013390023");

        phone.put("countryCode", "55");
        phone.put("areaCode", "11");
        phone.put("number", "22226842");

        shippingAddress.put("city", "Sao Paulo");
        shippingAddress.put("district", "Itaim BiBi");
        shippingAddress.put("street", "Av. Brigadeiro Faria Lima");
        shippingAddress.put("streetNumber", "3064");
        shippingAddress.put("state", "SP");
        shippingAddress.put("country", "BRA");
        shippingAddress.put("zipCode", "01451001");

        customerRequestBody.put("ownId", "pafsfpksfpasfoaijsfoajsf123");
        customerRequestBody.put("fullname", "Test Moip da Silva");
        customerRequestBody.put("email", "test.moip@mail.com");
        customerRequestBody.put("birthDate", "1980-5-10");
        customerRequestBody.put("taxDocument", taxDocument);
        customerRequestBody.put("phone", phone);
        customerRequestBody.put("shippingAddress", shippingAddress);

        return customerRequestBody;
    }

    @Test
    public void testCreate() {
        Map<String, Object> myCustomer = createCustomer();
        
        System.out.println(customer.create(myCustomer, setup));
    }
}
