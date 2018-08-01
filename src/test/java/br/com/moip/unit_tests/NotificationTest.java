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
import static org.junit.Assert.assertTrue;

public class NotificationTest {

    @Rule
    public Player player = new Player();
    private Setup setup;
    private Map<String, Object> body;
    private Parser parser;

    @Before
    public void initialize() {
        this.body = new HashMap<>();
        this.setup = new SetupFactory().setupBasicAuth(player.getURL("").toString());
        this.parser = new Parser();
    }

    @Play("notification/create_notification_preferences")
    @Test
    public void createNotificationPreferenceTest() {

        Map<String, Object> notification = Moip.API.notificationPreferences().create(body, setup);

        List events = parser.objectToList(notification.get("events"));
        assertEquals("ORDER.*", events.get(0));
        assertEquals("PAYMENT.AUTHORIZED", events.get(1));
        assertEquals("PAYMENT.CANCELLED", events.get(2));

        assertEquals("http://requestb.in/1dhjesw1", notification.get("target"));
        assertEquals("WEBHOOK", notification.get("media"));
        assertEquals("1457cbc9e2ab4bc7a62ab24951a9a878", notification.get("token"));
        assertEquals("NPR-LQCZEI6Q7I1G", notification.get("id"));
    }

    @Play("notification/get_notification_preferences")
    @Test
    public void getNotificationPreferenceTest() {

        Map<String, Object> notification = Moip.API.notificationPreferences().get("NPR-LQCZEI6Q7I1G", setup);

        List events = parser.objectToList(notification.get("events"));
        assertEquals("ORDER.*", events.get(0));
        assertEquals("PAYMENT.AUTHORIZED", events.get(1));
        assertEquals("PAYMENT.CANCELLED", events.get(2));

        assertEquals("http://requestb.in/1dhjesw1", notification.get("target"));
        assertEquals("WEBHOOK", notification.get("media"));
        assertEquals("1457cbc9e2ab4bc7a62ab24951a9a878", notification.get("token"));
        assertEquals("NPR-LQCZEI6Q7I1G", notification.get("id"));
    }

    @Play("notification/list_notification_preferences")
    @Test
    public void listNotificationPreferencesTest() {

        List<Map<String, Object>> listPreferences = Moip.API.notificationPreferences().list(setup);

        // listPreferences[0]
        List events0 = parser.objectToList(listPreferences.get(0).get("events"));
        assertEquals("ORDER.*", events0.get(0));
        assertEquals("PAYMENT.AUTHORIZED", events0.get(1));
        assertEquals("PAYMENT.CANCELLED", events0.get(2));

        assertEquals("http://my.uri/0", listPreferences.get(0).get("target"));
        assertEquals("WEBHOOK", listPreferences.get(0).get("media"));
        assertEquals("63c351458ccc4967aaa84f66a79c2be6", listPreferences.get(0).get("token"));
        assertEquals("NPR-O019G1CWTFV5", listPreferences.get(0).get("id"));

        // listPreferences[1]
        List events1 = parser.objectToList(listPreferences.get(1).get("events"));
        assertEquals("ORDER.*", events1.get(0));
        assertEquals("PAYMENT.AUTHORIZED", events1.get(1));
        assertEquals("PAYMENT.CANCELLED", events1.get(2));

        assertEquals("http://my.uri/1", listPreferences.get(1).get("target"));
        assertEquals("WEBHOOK", listPreferences.get(1).get("media"));
        assertEquals("688e2f2b75df4b968cad7d2b5c9c02d6", listPreferences.get(1).get("token"));
        assertEquals("NPR-AV7URX6E5OUF", listPreferences.get(1).get("id"));

        // listPreferences[2]
        List events2 = parser.objectToList(listPreferences.get(2).get("events"));
        assertEquals("ORDER.*", events2.get(0));
        assertEquals("PAYMENT.AUTHORIZED", events2.get(1));
        assertEquals("PAYMENT.CANCELLED", events2.get(2));

        assertEquals("http://my.uri/2", listPreferences.get(2).get("target"));
        assertEquals("WEBHOOK", listPreferences.get(2).get("media"));
        assertEquals("19160e43b6df4f58b31bfe57d291cb8a", listPreferences.get(2).get("token"));
        assertEquals("NPR-KCC2M2IYRQXJ", listPreferences.get(2).get("id"));

        // listPreferences[3]
        List events3 = parser.objectToList(listPreferences.get(3).get("events"));
        assertEquals("ORDER.*", events3.get(0));
        assertEquals("PAYMENT.AUTHORIZED", events3.get(1));
        assertEquals("PAYMENT.CANCELLED", events3.get(2));

        assertEquals("http://my.uri/3", listPreferences.get(3).get("target"));
        assertEquals("WEBHOOK", listPreferences.get(3).get("media"));
        assertEquals("a07451ff754449d18fedf60c6582e130", listPreferences.get(3).get("token"));
        assertEquals("NPR-GFD5Q9JP0JHO", listPreferences.get(3).get("id"));

        // listPreferences[4]
        List events4 = parser.objectToList(listPreferences.get(4).get("events"));
        assertEquals("ORDER.*", events4.get(0));
        assertEquals("PAYMENT.AUTHORIZED", events4.get(1));
        assertEquals("PAYMENT.CANCELLED", events4.get(2));

        assertEquals("http://my.uri/4", listPreferences.get(4).get("target"));
        assertEquals("WEBHOOK", listPreferences.get(4).get("media"));
        assertEquals("dc5c0470a44145baad470693b1ac7416", listPreferences.get(4).get("token"));
        assertEquals("NPR-F0TNENDS2PW0", listPreferences.get(4).get("id"));
    }

    @Play("notification/remove_notification_preferences")
    @Test
    public void deleteNotificationPreferenceTest() {

        Map<String, Object> removePreference = Moip.API.notificationPreferences().remove("NPR-KCC2M2IYRQXJ", setup);

        assertTrue(removePreference.isEmpty());
    }

    @Play("notification/get_webhooks")
    @Test
    public void getWebhooksTest() {

        Map<String, Object> getWebhooks = Moip.API.webhooks().get("ORD-W496X7T9FVGZ", setup);

        List<Map<String, Object>> webhooks = parser.objectToList(getWebhooks.get("webhooks"));

        // webhooks[0]
        assertEquals("ORD-W496X7T9FVGZ", webhooks.get(0).get("resourceId"));
        assertEquals("ORDER.PAID", webhooks.get(0).get("event"));
        assertEquals("https://requestb.in/16pw5ncfjleofweojfewbl1", webhooks.get(0).get("url"));
        assertEquals("FAILED", webhooks.get(0).get("status"));
        assertEquals("EVE-VP3PTS0QP294", webhooks.get(0).get("id"));
        assertEquals("2018-07-17T18:11:07.473Z", webhooks.get(0).get("sentAt"));

        // webhooks[1]
        assertEquals("ORD-W496X7T9FVGZ", webhooks.get(1).get("resourceId"));
        assertEquals("ORDER.WAITING", webhooks.get(1).get("event"));
        assertEquals("https://requestb.in/16pw5ncfjleofweojfewbl1", webhooks.get(1).get("url"));
        assertEquals("FAILED", webhooks.get(1).get("status"));
        assertEquals("EVE-YA8Z6XXDZJCY", webhooks.get(1).get("id"));
        assertEquals("2018-07-17T18:11:06.047Z", webhooks.get(1).get("sentAt"));

        // webhooks[2]
        assertEquals("ORD-W496X7T9FVGZ", webhooks.get(2).get("resourceId"));
        assertEquals("ORDER.CREATED", webhooks.get(2).get("event"));
        assertEquals("https://requestb.in/16pw5ncfjleofweojfewbl1", webhooks.get(2).get("url"));
        assertEquals("FAILED", webhooks.get(2).get("status"));
        assertEquals("EVE-FXFWBXTD4CBM", webhooks.get(2).get("id"));
        assertEquals("2018-07-17T18:11:02.628Z", webhooks.get(2).get("sentAt"));

        // webhooks[3]
        assertEquals("ORD-W496X7T9FVGZ", webhooks.get(3).get("resourceId"));
        assertEquals("ORDER.PAID", webhooks.get(3).get("event"));
        assertEquals("https://requestb.in/16pw5ncfjleofweojfewbl1", webhooks.get(3).get("url"));
        assertEquals("FAILED", webhooks.get(3).get("status"));
        assertEquals("EVE-Z0H3OSCO6CG4", webhooks.get(3).get("id"));
        assertEquals("2018-07-17T18:10:57.162Z", webhooks.get(3).get("sentAt"));

        // webhooks[4]
        assertEquals("ORD-W496X7T9FVGZ", webhooks.get(4).get("resourceId"));
        assertEquals("ORDER.WAITING", webhooks.get(4).get("event"));
        assertEquals("https://requestb.in/16pw5ncfjleofweojfewbl1", webhooks.get(4).get("url"));
        assertEquals("FAILED", webhooks.get(4).get("status"));
        assertEquals("EVE-BM1MIMKDV6VR", webhooks.get(4).get("id"));
        assertEquals("2018-07-17T18:10:55.716Z", webhooks.get(4).get("sentAt"));

        // webhooks[5]
        assertEquals("ORD-W496X7T9FVGZ", webhooks.get(5).get("resourceId"));
        assertEquals("ORDER.CREATED", webhooks.get(5).get("event"));
        assertEquals("https://requestb.in/16pw5ncfjleofweojfewbl1", webhooks.get(5).get("url"));
        assertEquals("FAILED", webhooks.get(5).get("status"));
        assertEquals("EVE-HR1TKRMBBV6P", webhooks.get(5).get("id"));
        assertEquals("2018-07-17T18:10:52.241Z", webhooks.get(5).get("sentAt"));
    }

    @Play("notification/list_webhooks")
    @Test
    public void listWebhooksTest() {

        Map<String, Object> listWebhooks = Moip.API.webhooks().list(setup);

        List<Map<String, Object>> webhooks = parser.objectToList(listWebhooks.get("webhooks"));

        // webhooks[0]
        assertEquals("ORD-W496X7T9FVGZ", webhooks.get(0).get("resourceId"));
        assertEquals("ORDER.PAID", webhooks.get(0).get("event"));
        assertEquals("https://requestb.in/16pw5ncfjleofweojfewbl1", webhooks.get(0).get("url"));
        assertEquals("FAILED", webhooks.get(0).get("status"));
        assertEquals("EVE-VP3PTS0QP294", webhooks.get(0).get("id"));
        assertEquals("2018-07-17T18:11:07.473Z", webhooks.get(0).get("sentAt"));

        // webhooks[1]
        assertEquals("PAY-NFSY35SXVCCN", webhooks.get(1).get("resourceId"));
        assertEquals("PAYMENT.AUTHORIZED", webhooks.get(1).get("event"));
        assertEquals("https://requestb.in/16pw5ncfjleofweojfewbl1", webhooks.get(1).get("url"));
        assertEquals("FAILED", webhooks.get(1).get("status"));
        assertEquals("EVE-O8EC0FBUBBAP", webhooks.get(1).get("id"));
        assertEquals("2018-07-17T18:11:07.303Z", webhooks.get(1).get("sentAt"));

        // webhooks[2]
        assertEquals("PAY-NFSY35SXVCCN", webhooks.get(2).get("resourceId"));
        assertEquals("PAYMENT.IN_ANALYSIS", webhooks.get(2).get("event"));
        assertEquals("https://requestb.in/16pw5ncfjleofweojfewbl1", webhooks.get(2).get("url"));
        assertEquals("FAILED", webhooks.get(2).get("status"));
        assertEquals("EVE-6QJN2PWF6SOD", webhooks.get(2).get("id"));
        assertEquals("2018-07-17T18:11:06.228Z", webhooks.get(2).get("sentAt"));

        // webhooks[3]
        assertEquals("ORD-W496X7T9FVGZ", webhooks.get(3).get("resourceId"));
        assertEquals("ORDER.WAITING", webhooks.get(3).get("event"));
        assertEquals("https://requestb.in/16pw5ncfjleofweojfewbl1", webhooks.get(3).get("url"));
        assertEquals("FAILED", webhooks.get(3).get("status"));
        assertEquals("EVE-YA8Z6XXDZJCY", webhooks.get(3).get("id"));
        assertEquals("2018-07-17T18:11:06.047Z", webhooks.get(3).get("sentAt"));
    }
}
