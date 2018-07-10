package br.com.moip.api;

import br.com.moip.models.Customer;
import br.com.moip.models.Order;
import br.com.moip.models.Payment;
import br.com.moip.models.Refund;
import br.com.moip.models.NotificationPreference;
import br.com.moip.models.Webhook;

public class APIResources {

    private static Customer customerInstance = new Customer();
    private static Order orderInstance = new Order();
    private static Payment paymentInstance = new Payment();
    private static Refund refundInstance = new Refund();
    private static NotificationPreference notificationPreferenceInstance = new NotificationPreference();
    private static Webhook webhookInstance = new Webhook();

    /**
     * This method is used to get a instance of customer object.
     *
     * @return  {@code Customer}
     */
    public static Customer customers() { return customerInstance; }

    public static Order orders() { return orderInstance; }

    public static Payment payments() { return paymentInstance; }

    public static Refund refunds() { return refundInstance; }

    public static NotificationPreference notificationPreferences() { return notificationPreferenceInstance; }

    public static Webhook webhooks() { return webhookInstance; }
}
