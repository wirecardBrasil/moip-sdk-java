package br.com.moip.integration_tests;

import br.com.moip.Moip;
import br.com.moip.auth.Authentication;
import br.com.moip.auth.BasicAuth;
import br.com.moip.exception.UnexpectedException;
import br.com.moip.exception.ValidationException;
import br.com.moip.models.Setup;

import static br.com.moip.helpers.PayloadFactory.payloadFactory;
import static br.com.moip.helpers.PayloadFactory.value;

import java.util.HashMap;
import java.util.Map;

public class CustomerTest {

    private final static String token = "01010101010101010101010101010101";
    private final static String key = "ABABABABABABABABABABABABABABABABABABABAB";

    private static Authentication auth = new BasicAuth(token, key);

    private Setup setup = new Setup().setAuthentication(auth).setEnvironment(Setup.Environment.SANDBOX);

    public void createCustomerTest() {

        Map<String, Object> taxDocument = payloadFactory(
                value("type", "CPF"),
                value("number", "10013390023")
        );

        Map<String, Object> phone = payloadFactory(
                value("countryCode", "55"),
                value("areaCode", "11"),
                value("number", "22226842")
        );

        Map<String, Object> shippingAddress = payloadFactory(
                value("city", "Sao Paulo"),
                value("district", "Itaim BiBi"),
                value("street", "Av. Brigadeiro Faria Lima"),
                value("streetNumber", "3064"),
                value("state", "SP"),
                value("country", "BRA"),
                value("zipCode", "01451001")
        );

        Map<String, Object> customerRequestBody = payloadFactory(
                value("ownId", "amslmfas12431mfpa"),
                value("fullname", "Test Moip da Silva"),
                value("email", "test.moip@mail.com"),
                value("birthDate", "1980-5-10"),
                value("taxDocument", taxDocument),
                value("phone", phone),
                value("shippingAddress", shippingAddress)
        );

        try {

            Map<String, Object> responseCreation = Moip.API.customers().create(customerRequestBody, setup);

            System.out.println("create: " + responseCreation);

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

    public void addCreditCardTest () {

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

        Map<String, Object> addCreditCard = new HashMap<>();
        addCreditCard.put("method", "CREDIT_CARD");
        addCreditCard.put("creditCard", creditCard);

        try {

            Map<String, Object> addCCResponse = Moip.API.customers().addCreditCard(addCreditCard, "CUS-THU3I1K97KN0", setup);

            System.out.println("add credit card: " + addCCResponse);

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

    public void deleteCreditCardTest() {

        try {

            Map<String, Object> deleteCreditCardResponse = Moip.API.customers().deleteCreditCard("CRC-6KGGEP72ZREJ", setup);

            System.out.println("delete credit card: " + deleteCreditCardResponse);

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

    public void getCustomerTest() {

        try {

            Map<String, Object> responseGet = Moip.API.customers().get("CUS-THU3I1K97KN0", setup);

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

    public void listCustomersTest() {

        try {

            Map<String, Object> listResponse = Moip.API.customers().list(setup);

            System.out.println("list: " + listResponse);

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
