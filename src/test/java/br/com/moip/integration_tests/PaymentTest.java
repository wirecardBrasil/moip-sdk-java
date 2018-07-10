package br.com.moip.integration_tests;

import br.com.moip.Moip;
import br.com.moip.auth.Authentication;
import br.com.moip.auth.BasicAuth;
import br.com.moip.exception.UnexpectedException;
import br.com.moip.exception.ValidationException;
import br.com.moip.models.Setup;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PaymentTest {

    private final static String token = "01010101010101010101010101010101";
    private final static String key = "ABABABABABABABABABABABABABABABABABABABAB";

    private static Authentication auth = new BasicAuth(token, key);

    private Setup setup = new Setup().setAuthentication(auth).setEnvironment(Setup.Environment.SANDBOX);

    @Test
    public void createPaymentTest() {

        String hash = "CCbe2TVHIsINX+v+bVPP0KSKWfXs6AtrJlHznaSdTgmvNFOvsalZ7pFgoddc3fH7vEdpxCa55ed1DoNIz" +
                "WWUo3+7KLQgV0Gi/ux0RShBiNzB0wiFf+OBef9x7b3IgcdulZqdNGn84AvGInJ9r6a2iZ8kY0xQ1xONOod5rVku" +
                "GDvNgVVOIB7Zs5In9j3f4TENRU7aqx63srT3UPP+wsQBMNJ4wQturogjZBhDIQqEm2sC9Zzx+E8zP/aZ+YBF9O+" +
                "nfeofF4S2E9bs4A9neWCivntqwENX0O755NamWX/up2MbyHu8N+cm493RrbUT76jibB0RyyT9nkKpSOuJIGU8vA==";

        Map<String, Object> taxDocument = new HashMap<>();
        taxDocument.put("type", "CPF");
        taxDocument.put("number", "33333333333");

        Map<String, Object> phone = new HashMap<>();
        phone.put("countryCode", "55");
        phone.put("areaCode", "11");
        phone.put("number", "66778899");

        Map<String, Object> holder = new HashMap<>();
        holder.put("fullname", "Portador Teste Moip");
        holder.put("birthdate", "1988-12-30");
        holder.put("taxDocument", taxDocument);
        holder.put("phone", phone);

        Map<String, Object> creditCard = new HashMap<>();
        creditCard.put("hash", hash);
        creditCard.put("store", false);
        creditCard.put("holder", holder);

        Map<String, Object> fundingInstrument = new HashMap<>();
        fundingInstrument.put("method", "CREDIT_CARD");
        fundingInstrument.put("creditCard", creditCard);

        Map<String, Object> paymentBody = new HashMap<>();
        paymentBody.put("installmentCount", 1);
        paymentBody.put("statementDescriptor", "minhaLoja.com");
        paymentBody.put("fundingInstrument", fundingInstrument);

        try {

            Map<String, Object> payResponse = Moip.API.payments().pay(paymentBody, "ORD-TPCAFYL5L3AU", setup);

            System.out.println("create: " + payResponse);

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
    public void createPreAuthorizedTest() {

        String hash = "CCbe2TVHIsINX+v+bVPP0KSKWfXs6AtrJlHznaSdTgmvNFOvsalZ7pFgoddc3fH7vEdpxCa55ed1DoNIz" +
                "WWUo3+7KLQgV0Gi/ux0RShBiNzB0wiFf+OBef9x7b3IgcdulZqdNGn84AvGInJ9r6a2iZ8kY0xQ1xONOod5rVku" +
                "GDvNgVVOIB7Zs5In9j3f4TENRU7aqx63srT3UPP+wsQBMNJ4wQturogjZBhDIQqEm2sC9Zzx+E8zP/aZ+YBF9O+" +
                "nfeofF4S2E9bs4A9neWCivntqwENX0O755NamWX/up2MbyHu8N+cm493RrbUT76jibB0RyyT9nkKpSOuJIGU8vA==";

        Map<String, Object> taxDocument = new HashMap<>();
        taxDocument.put("type", "CPF");
        taxDocument.put("number", "33333333333");

        Map<String, Object> phone = new HashMap<>();
        phone.put("countryCode", "55");
        phone.put("areaCode", "11");
        phone.put("number", "66778899");

        Map<String, Object> holder = new HashMap<>();
        holder.put("fullname", "Portador Teste Moip");
        holder.put("birthdate", "1988-12-30");
        holder.put("taxDocument", taxDocument);
        holder.put("phone", phone);

        Map<String, Object> creditCard = new HashMap<>();
        creditCard.put("hash", hash);
        creditCard.put("store", false);
        creditCard.put("holder", holder);

        Map<String, Object> fundingInstrument = new HashMap<>();
        fundingInstrument.put("method", "CREDIT_CARD");
        fundingInstrument.put("creditCard", creditCard);

        Map<String, Object> paymentBody = new HashMap<>();
        paymentBody.put("installmentCount", 1);
        paymentBody.put("statementDescriptor", "minhaLoja.com");
        paymentBody.put("fundingInstrument", fundingInstrument);
        paymentBody.put("delayCapture", true);

        try {

            Map<String, Object> preAuthorized = Moip.API.payments().pay(paymentBody, "ORD-S4AAX3GYUDWR", setup);

            System.out.println("pre-authorized: " + preAuthorized);

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
    public void capturePreAuthorizedTest() {

        try {

            Map<String, Object> convert = Moip.API.payments().capturePreAuthorized("PAY-CUB8GVW4HGR7", setup);

            System.out.println("capture: " + convert);

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
    public void cancelPreAuthorizedTest() {

        try {

            Map<String, Object> cancel = Moip.API.payments().cancelPreAuthorized("PAY-I2RXHXRGJ6GO", setup);

            System.out.println("cancel: " + cancel);

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
    public void getPaymentTest() {

        try {

            Map<String, Object> getResponse = Moip.API.payments().get("PAY-I2RXHXRGJ6GO", setup);

            System.out.println("get: " + getResponse);

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
    public void authorizePaymentTest() {

        try {

            Map<String, Object> authorizeResponse = Moip.API.payments().authorize("PAY-7VC4M6K4IVG1", 9500, setup);

            System.out.println("authorize: " + authorizeResponse);

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
