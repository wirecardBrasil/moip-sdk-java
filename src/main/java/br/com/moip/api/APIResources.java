package br.com.moip.api;

import br.com.moip.models.*;
import br.com.moip.models.Accounts;

public class APIResources {

    private static Customers customerInstance = new Customers();
    private static Orders orderInstance = new Orders();
    private static Payments paymentInstance = new Payments();
    private static Refunds refundInstance = new Refunds();
    private static NotificationPreferences notificationPreferenceInstance = new NotificationPreferences();
    private static Webhooks webhookInstance = new Webhooks();
    private static Accounts accountInstance = new Accounts();
    private static Connect connectInstance = new Connect();
    private static Multiorders multiorderInstance = new Multiorders();

    /**
     * This method is used to get a instance of customer object.
     *
     * @return  {@code Customers}
     */
    public static Customers customers() { return customerInstance; }

    public static Orders orders() { return orderInstance; }

    public static Payments payments() { return paymentInstance; }

    public static Refunds refunds() { return refundInstance; }

    public static NotificationPreferences notificationPreferences() { return notificationPreferenceInstance; }

    public static Webhooks webhooks() { return webhookInstance; }

    public static Accounts accounts() { return accountInstance; }

    public static Connect connect() { return connectInstance; }

    public static Multiorders multiorders() {return multiorderInstance; }
}
