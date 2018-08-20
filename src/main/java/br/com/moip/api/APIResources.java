package br.com.moip.api;

import br.com.moip.models.Customers;
import br.com.moip.models.Orders;
import br.com.moip.models.Payments;
import br.com.moip.models.Refunds;
import br.com.moip.models.NotificationPreferences;
import br.com.moip.models.Webhooks;
import br.com.moip.models.Accounts;
import br.com.moip.models.Connect;
import br.com.moip.models.Multiorders;
import br.com.moip.models.Multipayments;
import br.com.moip.models.BankAccounts;
import br.com.moip.models.Balances;
import br.com.moip.models.Entries;
import br.com.moip.models.Transfers;
import br.com.moip.models.Escrows;

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
    private static Multipayments multipaymentsInstance = new Multipayments();
    private static BankAccounts bankAccountsInstance = new BankAccounts();
    private static Balances balancesInstance = new Balances();
    private static Entries entriesInstance = new Entries();
    private static Transfers transfersInstance = new Transfers();
    private static Escrows escrowsInstance = new Escrows();

    public static Customers customers() { return customerInstance; }

    public static Orders orders() { return orderInstance; }

    public static Payments payments() { return paymentInstance; }

    public static Refunds refunds() { return refundInstance; }

    public static NotificationPreferences notificationPreferences() { return notificationPreferenceInstance; }

    public static Webhooks webhooks() { return webhookInstance; }

    public static Accounts accounts() { return accountInstance; }

    public static Connect connect() { return connectInstance; }

    public static Multiorders multiorders() { return multiorderInstance; }

    public static Multipayments multipayments() { return multipaymentsInstance; }

    public static BankAccounts bankAccounts() { return bankAccountsInstance; }

    public static Balances balances() { return balancesInstance; }

    public static Entries entries() { return entriesInstance; }

    public static Transfers transfers() { return transfersInstance; }

    public static Escrows escrows() { return escrowsInstance; }
}
