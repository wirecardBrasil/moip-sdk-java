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

public class NotificationPreferenceTest {

    private final static String token = "01010101010101010101010101010101";
    private final static String key = "ABABABABABABABABABABABABABABABABABABABAB";

    private static Authentication auth = new BasicAuth(token, key);
    private Setup setup = new Setup().setAuthentication(auth).setEnvironment(Setup.Environment.SANDBOX);

    @Test
    public void createNotificationPreferenceTest() {
        List events = new ArrayList();
        events.add("ORDER.*");
        events.add("PAYMENT.AUTHORIZED");
        events.add("PAYMENT.CANCELLED");

        // Create notification preference test //
        Map<String, Object> body = new HashMap<>();
        body.put("events", events);
        body.put("target", "http://requestb.in/1dhjesw1");
        body.put("media", "WEBHOOK");

        try {

            Map<String, Object> response = Moip.API.notificationPreferences().create(body, setup);

            System.out.println(response); // id: NPR-PFDKZLYIROZW

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
    public void getNotificationPreferenceTest() {

        try {
            Map<String, Object> response = Moip.API.notificationPreferences().get("NPR-PFDKZLYIROZW", setup);

            System.out.println(response);

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
    public void listNotificationPreferenceTest() {

        try {

            List<Map<String, Object>> response = Moip.API.notificationPreferences().list(setup);

            System.out.println(response);

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
    public void removeNotificationPreferenceTest() {

        try {

            Map<String, Object> response = Moip.API.notificationPreferences().remove("NPR-KSF5U90I3B20", setup);

            System.out.println(response);

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
