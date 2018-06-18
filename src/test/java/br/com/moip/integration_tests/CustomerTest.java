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

        customerRequestBody.put("ownId", "afosjnfajneajnskmdanje12124");
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

        // Create customer test
        Map<String, Object> myCustomer = createCustomer();

        Map<String, Object> responseCreation = customer.create(myCustomer, setup);

        System.out.println("create: " + responseCreation);

        // Add credit card test
        Map<String, Object> taxDocumentHolder = new HashMap<>();
        taxDocumentHolder.put("type", "CPF");
        taxDocumentHolder.put("number", "22288866644");

        Map<String, Object> phoneHolder = new HashMap<>();
        phoneHolder.put("countryCode", "55");
        phoneHolder.put("areaCode", "11");
        phoneHolder.put("number", "55552266");

        Map<String, Object> holder = new HashMap<>();
        holder.put("fullname", "Test Holder Moip");
        holder.put("birthdate", "1990-10-22");
        holder.put("taxDocument", taxDocumentHolder);
        holder.put("phone", phoneHolder);

        Map<String, Object> creditCard = new HashMap<>();
        creditCard.put("expirationMonth", "05");
        creditCard.put("expirationYear", "22");
        creditCard.put("number", "4012001037141112");
        creditCard.put("cvc", "123");
        creditCard.put("holder", holder);

        Map<String, Object> addCreditCardBody = new HashMap<>();
        addCreditCardBody.put("method", "CREDIT_CARD");
        addCreditCardBody.put("creditCard", creditCard);

        String customerId = responseCreation.get("id").toString();

        Map<String, Object> addCreditCardResponse = customer.addCreditCard(addCreditCardBody, customerId, setup);

        System.out.println("add credit card: " + addCreditCardResponse);

        // Delete credit card
        String creditCardId = addCreditCardResponse.get("id").toString();

        Map<String, Object> deleteCreditCardResponse = customer.deleteCreditCard(creditCardId, setup);

        System.out.println("delete credit card: " + deleteCreditCardResponse);

        // Get customer test
        Object id = responseCreation.get("id");

        Map<String, Object> responseGet = customer.get(id.toString(), setup);

        System.out.println("get: " + responseGet);

        // List Customer
        Map<String, Object> listResponse = customer.list(setup);

        System.out.println("list: " + listResponse);
    }
}
