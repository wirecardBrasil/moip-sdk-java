package br.com.moip.integration_tests;

import br.com.moip.Moip;
import br.com.moip.auth.Authentication;
import br.com.moip.auth.BasicAuth;
import br.com.moip.auth.OAuth;
import br.com.moip.exception.UnexpectedException;
import br.com.moip.exception.ValidationException;
import br.com.moip.models.Setup;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RefundTest {

    private final static String token = "01010101010101010101010101010101";
    private final static String key = "ABABABABABABABABABABABABABABABABABABABAB";

    private static Authentication auth = new BasicAuth(token, key);

    private static Authentication oAuth = new OAuth("8833c9eb036543b6b0acd685a76c9ead_v2");

    private Setup setup = new Setup().setAuthentication(oAuth).setEnvironment(Setup.Environment.SANDBOX);

    public void refundPaymentTest() {

        try {

            Map<String, Object> refundCC = Moip.API.refunds().refundPayment("PAY-E9DCD2A51HK5", setup);

            System.out.println(refundCC);

        } catch (ValidationException e) {
            System.out.println("Validation!");
            e.getErrors();
            System.out.println(e.getMessage());
            e.getStackTrace();
        } catch (UnexpectedException e) {
            System.out.println("Unexpected!");
            System.out.println(e);
            e.getStackTrace();
        }
    }

    public void refundBankAccountTest() {

        Map<String, Object> taxDocument = new HashMap<>();
        taxDocument.put("type", "CPF");
        taxDocument.put("number", "57390209674");


        Map<String, Object> holder = new HashMap<>();
        holder.put("fullname", "Jose Silva");
        holder.put("taxDocument", taxDocument);

        Map<String, Object> bankAccount = new HashMap<>();
        bankAccount.put("type", "CHECKING");
        bankAccount.put("bankNumber", "001");
        bankAccount.put("agencyNumber", 4444444);
        bankAccount.put("agencyCheckNumber", 2);
        bankAccount.put("accountNumber", 1234);
        bankAccount.put("accountCheckNumber", 1);
        bankAccount.put("holder", holder);

        Map<String, Object> refundingInstrument = new HashMap<>();
        refundingInstrument.put("method", "BANK_ACCOUNT");
        refundingInstrument.put("bankAccount", bankAccount);

        // Refund order bank account //
        Map<String, Object> refundBankAccountBody = new HashMap<>();
        refundBankAccountBody.put("refundingInstrument", refundingInstrument);

        try {

            Map<String, Object> refundBankAccount = Moip.API.refunds().refundOrder(refundBankAccountBody, "ORD-477LLUMK5J71", setup);

            System.out.println(refundBankAccount);

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

    public void refundMoipAccountTest() {

        Map<String, Object> moipAccount = new HashMap<>();
        moipAccount.put("id", "MPA-273HG64574Y7");


        Map<String, Object> refundingInstrument = new HashMap<>();
        refundingInstrument.put("method", "MOIP_ACCOUNT");
        refundingInstrument.put("moipAccount", moipAccount);

        // Partial refund payment Moip account test //
        Map<String, Object> refundMoipAccountBody = new HashMap<>();
        refundMoipAccountBody.put("amount", "500");
        refundMoipAccountBody.put("refundingInstrument", refundingInstrument);

        try {

            Map<String, Object> refundMoipAccount = Moip.API.refunds().refundPayment(refundMoipAccountBody, "PAY-E9DCD2A51HK5", setup);

            System.out.println(refundMoipAccount);

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

    public void getRefundTest() {
        Map<String, Object> get = Moip.API.refunds().get("REF-LA2XBT8UWNK5", setup);

        System.out.println(get);
    }

    public void listPaymentRefundsTest() {
        Map<String, Object> listPaymentRefund = Moip.API.refunds().listPaymentRefunds("PAY-E9DCD2A51HK5", setup);

        System.out.println(listPaymentRefund);
    }

    public void listOrderRefundsTest() {
        Map<String, Object> listOrderRefunds = Moip.API.refunds().listOrderRefunds("ORD-477LLUMK5J71", setup);

        System.out.println(listOrderRefunds);
    }
}
