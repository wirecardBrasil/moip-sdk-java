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

public class PaymentTest {

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

    @Play("payment/payment_with_credit_card")
    @Test
    public void paymentWithCreditCardTest() {

        Map<String, Object> payment = Moip.API.payments().pay(body, "ORD-MA1UMH2OJQIO", setup);

        assertEquals("PAY-WVCZZYDPRF22", payment.get("id"));
        assertEquals("IN_ANALYSIS", payment.get("status"));
        assertEquals(false, payment.get("delayCapture"));

        Map<String, Object> amount = parser.objectToMap(payment.get("amount"));
        assertEquals(11000, amount.get("total"));
        assertEquals(11000, amount.get("gross"));
        assertEquals(0, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(11000, amount.get("liquid"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(1, payment.get("installmentCount"));
        assertEquals("minhaLoja.com", payment.get("statementDescriptor"));

        Map<String, Object> fundingInstrument = parser.objectToMap(payment.get("fundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-CQC2YNRA6UUT", creditCard.get("id"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        Map<String, Object> holder = parser.objectToMap(creditCard.get("holder"));
        assertEquals("1988-12-30", holder.get("birthDate"));

        Map<String, Object> taxDocumentHolder = parser.objectToMap(holder.get("taxDocument"));
        assertEquals("CPF", taxDocumentHolder.get("type"));
        assertEquals("33333333333", taxDocumentHolder.get("number"));

        assertEquals("Teste Portador da Silva", holder.get("fullname"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> acquirerDetails = parser.objectToMap(payment.get("acquirerDetails"));
        assertEquals("123456", acquirerDetails.get("authorizationNumber"));

        Map<String, Object> taxDocument = parser.objectToMap(acquirerDetails.get("taxDocument"));
        assertEquals("CNPJ", taxDocument.get("type"));
        assertEquals("01027058000191", taxDocument.get("number"));

        List<Map<String, Object>> fees = parser.objectToList(payment.get("fees"));
        assertEquals("TRANSACTION", fees.get(0).get("type"));
        assertEquals(0, fees.get(0).get("amount"));

        List<Map<String, Object>> events = parser.objectToList(payment.get("events"));

        // events[0]
        assertEquals("PAYMENT.IN_ANALYSIS", events.get(0).get("type"));
        assertEquals("2018-07-16T11:22:52.568-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("PAYMENT.CREATED", events.get(1).get("type"));
        assertEquals("2018-07-16T11:22:50.088-03", events.get(1).get("createdAt"));

        List<Map<String, Object>> receivers = parser.objectToList(payment.get("receivers"));
        Map<String, Object> moipAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccount.get("id"));
        assertEquals("integracao@labs.moip.com.br", moipAccount.get("login"));
        assertEquals("Moip SandBox", moipAccount.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> amountReceived = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(11000, amountReceived.get("total"));
        assertEquals("BRL", amountReceived.get("currency"));
        assertEquals(0, amountReceived.get("fees"));
        assertEquals(0, amountReceived.get("refunds"));

        assertEquals(true, receivers.get(0).get("feePayor"));

        Map<String, Object> device = parser.objectToMap(payment.get("device"));
        assertEquals("127.0.0.1", device.get("ip"));

        Map<String, Object> geolocation = parser.objectToMap(device.get("geolocation"));
        assertEquals(-33.867, geolocation.get("latitude"));
        assertEquals(151.206, geolocation.get("longitude"));

        assertEquals("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36", device.get("userAgent"));
        assertEquals("QAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolp", device.get("fingerprint"));

        Map<String, Object> links = parser.objectToMap(payment.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-WVCZZYDPRF22", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-MA1UMH2OJQIO", order.get("href"));
        assertEquals("ORD-MA1UMH2OJQIO", order.get("title"));

        assertEquals("2018-07-16T11:22:50.061-03", payment.get("createdAt"));
        assertEquals("2018-07-16T11:22:52.568-03", payment.get("updatedAt"));
    }

    @Play("payment/payment_with_boleto")
    @Test
    public void paymentWithBoletoTest() {

        Map<String, Object> payment = Moip.API.payments().pay(body, "ORD-9S0XI6VIMTJ7", setup);

        assertEquals("PAY-9SGAMYL775UX", payment.get("id"));
        assertEquals("WAITING", payment.get("status"));
        assertEquals(false, payment.get("delayCapture"));

        Map<String, Object> amount = parser.objectToMap(payment.get("amount"));
        assertEquals(11000, amount.get("total"));
        assertEquals(11000, amount.get("gross"));
        assertEquals(0, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(11000, amount.get("liquid"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(1, payment.get("installmentCount"));

        Map<String, Object> fundingInstrument = parser.objectToMap(payment.get("fundingInstrument"));
        Map<String, Object> boleto = parser.objectToMap(fundingInstrument.get("boleto"));
        assertEquals("2020-06-20", boleto.get("expirationDate"));
        assertEquals("00190.00009 01014.051005 00000.787176 7 72370000001000", boleto.get("lineCode"));
        assertEquals("http://www.lojaexemplo.com.br/logo.jpg", boleto.get("logoUri"));

        Map<String, Object> instructionLines = parser.objectToMap(boleto.get("instructionLines"));
        assertEquals("Atenção,", instructionLines.get("first"));
        assertEquals("fique atento à data de vencimento do boleto.", instructionLines.get("second"));
        assertEquals("Pague em qualquer casa lotérica.", instructionLines.get("third"));

        assertEquals("BOLETO", fundingInstrument.get("method"));

        List<Map<String, Object>> fees = parser.objectToList(payment.get("fees"));
        assertEquals("TRANSACTION", fees.get(0).get("type"));
        assertEquals(0, fees.get(0).get("amount"));

        List<Map<String, Object>> events = parser.objectToList(payment.get("events"));

        // events[0]
        assertEquals("PAYMENT.CREATED", events.get(0).get("type"));
        assertEquals("2018-07-16T13:22:41.284-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("PAYMENT.WAITING", events.get(1).get("type"));
        assertEquals("2018-07-16T13:22:41.284-03", events.get(1).get("createdAt"));

        List<Map<String, Object>> receivers = parser.objectToList(payment.get("receivers"));
        Map<String, Object> moipAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccount.get("id"));
        assertEquals("integracao@labs.moip.com.br", moipAccount.get("login"));
        assertEquals("Moip SandBox", moipAccount.get("fullname"));

        Map<String, Object> amountReceived = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(11000, amountReceived.get("total"));
        assertEquals("BRL", amountReceived.get("currency"));
        assertEquals(0, amountReceived.get("fees"));
        assertEquals(0, amountReceived.get("refunds"));

        assertEquals(true, receivers.get(0).get("feePayor"));

        Map<String, Object> links = parser.objectToMap(payment.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-9SGAMYL775UX", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-9S0XI6VIMTJ7", order.get("href"));
        assertEquals("ORD-9S0XI6VIMTJ7", order.get("title"));

        assertEquals("2018-07-16T13:22:41.282-03", payment.get("createdAt"));
        assertEquals("2018-07-16T13:22:41.282-03", payment.get("updatedAt"));
    }

    @Play("payment/payment_with_online_bank_debit")
    @Test
    public void paymentWithOnlineBankDebitTest() {

        Map<String, Object> payment = Moip.API.payments().pay(body, "ORD-GVEF677TOU7D", setup);

        assertEquals("PAY-SKEH9F7N532S", payment.get("id"));
        assertEquals("WAITING", payment.get("status"));
        assertEquals(false, payment.get("delayCapture"));

        Map<String, Object> amount = parser.objectToMap(payment.get("amount"));
        assertEquals(11000, amount.get("total"));
        assertEquals(11000, amount.get("gross"));
        assertEquals(277, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(10723, amount.get("liquid"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(1, payment.get("installmentCount"));

        Map<String, Object> fundingInstrument = parser.objectToMap(payment.get("fundingInstrument"));
        assertEquals("ONLINE_BANK_DEBIT", fundingInstrument.get("method"));

        Map<String, Object> onlineBankDebit = parser.objectToMap(fundingInstrument.get("onlineBankDebit"));
        assertEquals("341", onlineBankDebit.get("bankNumber"));
        assertEquals("2018-11-22", onlineBankDebit.get("expirationDate"));
        assertEquals("BANCO ITAU S.A.", onlineBankDebit.get("bankName"));

        List<Map<String, Object>> fees = parser.objectToList(payment.get("fees"));
        assertEquals("TRANSACTION", fees.get(0).get("type"));
        assertEquals(277, fees.get(0).get("amount"));

        List<Map<String, Object>> events = parser.objectToList(payment.get("events"));

        // events[0]
        assertEquals("PAYMENT.CREATED", events.get(0).get("type"));
        assertEquals("2018-07-16T13:52:06.138-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("PAYMENT.WAITING", events.get(1).get("type"));
        assertEquals("2018-07-16T13:52:06.138-03", events.get(1).get("createdAt"));

        List<Map<String, Object>> receivers = parser.objectToList(payment.get("receivers"));
        Map<String, Object> moipAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccount.get("id"));
        assertEquals("integracao@labs.moip.com.br", moipAccount.get("login"));
        assertEquals("Moip SandBox", moipAccount.get("fullname"));

        Map<String, Object> amountReceived = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(11000, amountReceived.get("total"));
        assertEquals("BRL", amountReceived.get("currency"));
        assertEquals(0, amountReceived.get("fees"));
        assertEquals(0, amountReceived.get("refunds"));

        assertEquals(true, receivers.get(0).get("feePayor"));

        Map<String, Object> links = parser.objectToMap(payment.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-SKEH9F7N532S", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-GVEF677TOU7D", order.get("href"));
        assertEquals("ORD-GVEF677TOU7D", order.get("title"));

        Map<String, Object> payOnlineBankDebitItau = parser.objectToMap(links.get("payOnlineBankDebitItau"));
        assertEquals("https://checkout-sandbox.moip.com.br/debit/itau/PAY-SKEH9F7N532S", payOnlineBankDebitItau.get("redirectHref"));

        assertEquals("2018-07-16T13:52:06.136-03", payment.get("createdAt"));
        assertEquals("2018-07-16T13:52:06.136-03", payment.get("updatedAt"));
    }

    @Play("payment/pre_authorized_payment")
    @Test
    public void createPreAuthorizedPaymentTest() {

        Map<String, Object> payment = Moip.API.payments().pay(body, "ORD-J5F0JITE58DV", setup);

        assertEquals("PAY-MMU64JJBUI6Z", payment.get("id"));
        assertEquals("IN_ANALYSIS", payment.get("status"));
        assertEquals(true, payment.get("delayCapture"));

        Map<String, Object> amount = parser.objectToMap(payment.get("amount"));
        assertEquals(11000, amount.get("total"));
        assertEquals(11000, amount.get("gross"));
        assertEquals(0, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(11000, amount.get("liquid"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(3, payment.get("installmentCount"));
        assertEquals("Minha Loja", payment.get("statementDescriptor"));

        Map<String, Object> fundingInstrument = parser.objectToMap(payment.get("fundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-CQC2YNRA6UUT", creditCard.get("id"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        Map<String, Object> holder = parser.objectToMap(creditCard.get("holder"));
        assertEquals("1988-12-30", holder.get("birthDate"));

        Map<String, Object> taxDocumentHolder = parser.objectToMap(holder.get("taxDocument"));
        assertEquals("CPF", taxDocumentHolder.get("type"));
        assertEquals("78994193600", taxDocumentHolder.get("number"));

        Map<String, Object> billingAddress = parser.objectToMap(holder.get("billingAddress"));
        assertEquals("Avenida Contorno", billingAddress.get("street"));
        assertEquals("400", billingAddress.get("streetNumber"));
        assertEquals("Savassi", billingAddress.get("district"));
        assertEquals("Belo Horizonte", billingAddress.get("city"));
        assertEquals("MG", billingAddress.get("state"));
        assertEquals("BRA", billingAddress.get("country"));
        assertEquals("76932800", billingAddress.get("zipCode"));

        assertEquals("Roberto Oliveira", holder.get("fullname"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> acquirerDetails = parser.objectToMap(payment.get("acquirerDetails"));
        assertEquals("123456", acquirerDetails.get("authorizationNumber"));

        Map<String, Object> taxDocument = parser.objectToMap(acquirerDetails.get("taxDocument"));
        assertEquals("CNPJ", taxDocument.get("type"));
        assertEquals("01027058000191", taxDocument.get("number"));

        List<Map<String, Object>> fees = parser.objectToList(payment.get("fees"));
        assertEquals("TRANSACTION", fees.get(0).get("type"));
        assertEquals(0, fees.get(0).get("amount"));

        List<Map<String, Object>> events = parser.objectToList(payment.get("events"));

        // events[0]
        assertEquals("PAYMENT.IN_ANALYSIS", events.get(0).get("type"));
        assertEquals("2018-07-16T14:11:40.090-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("PAYMENT.CREATED", events.get(1).get("type"));
        assertEquals("2018-07-16T14:11:34.179-03", events.get(1).get("createdAt"));

        List<Map<String, Object>> receivers = parser.objectToList(payment.get("receivers"));
        Map<String, Object> moipAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccount.get("id"));
        assertEquals("integracao@labs.moip.com.br", moipAccount.get("login"));
        assertEquals("Moip SandBox", moipAccount.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> amountReceived = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(11000, amountReceived.get("total"));
        assertEquals("BRL", amountReceived.get("currency"));
        assertEquals(0, amountReceived.get("fees"));
        assertEquals(0, amountReceived.get("refunds"));

        assertEquals(true, receivers.get(0).get("feePayor"));

        Map<String, Object> links = parser.objectToMap(payment.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-MMU64JJBUI6Z", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-J5F0JITE58DV", order.get("href"));
        assertEquals("ORD-J5F0JITE58DV", order.get("title"));

        assertEquals("2018-07-16T14:11:34.152-03", payment.get("createdAt"));
        assertEquals("2018-07-16T14:11:40.090-03", payment.get("updatedAt"));
    }

    @Play("payment/capture_pre_authorized")
    @Test
    public void capturePreAuthorizedTest() {

        Map<String, Object> capture = Moip.API.payments().capturePreAuthorized("PAY-MMU64JJBUI6Z", setup);

        assertEquals("PAY-MMU64JJBUI6Z", capture.get("id"));
        assertEquals("AUTHORIZED", capture.get("status"));
        assertEquals(true, capture.get("delayCapture"));

        Map<String, Object> amount = parser.objectToMap(capture.get("amount"));
        assertEquals(11000, amount.get("total"));
        assertEquals(11000, amount.get("gross"));
        assertEquals(1127, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(9873, amount.get("liquid"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(3, capture.get("installmentCount"));
        assertEquals("Minha Loja", capture.get("statementDescriptor"));

        Map<String, Object> fundingInstrument = parser.objectToMap(capture.get("fundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-CQC2YNRA6UUT", creditCard.get("id"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        Map<String, Object> holder = parser.objectToMap(creditCard.get("holder"));
        assertEquals("1988-12-30", holder.get("birthDate"));

        Map<String, Object> taxDocumentHolder = parser.objectToMap(holder.get("taxDocument"));
        assertEquals("CPF", taxDocumentHolder.get("type"));
        assertEquals("78994193600", taxDocumentHolder.get("number"));

        Map<String, Object> billingAddress = parser.objectToMap(holder.get("billingAddress"));
        assertEquals("Avenida Contorno", billingAddress.get("street"));
        assertEquals("400", billingAddress.get("streetNumber"));
        assertEquals("Savassi", billingAddress.get("district"));
        assertEquals("Belo Horizonte", billingAddress.get("city"));
        assertEquals("MG", billingAddress.get("state"));
        assertEquals("BRA", billingAddress.get("country"));
        assertEquals("76932800", billingAddress.get("zipCode"));

        assertEquals("Roberto Oliveira", holder.get("fullname"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> acquirerDetails = parser.objectToMap(capture.get("acquirerDetails"));
        assertEquals("123456", acquirerDetails.get("authorizationNumber"));

        Map<String, Object> taxDocument = parser.objectToMap(acquirerDetails.get("taxDocument"));
        assertEquals("CNPJ", taxDocument.get("type"));
        assertEquals("01027058000191", taxDocument.get("number"));

        List<Map<String, Object>> fees = parser.objectToList(capture.get("fees"));

        // fees[0]
        assertEquals("TRANSACTION", fees.get(0).get("type"));
        assertEquals(585, fees.get(0).get("amount"));

        // fees[1]
        assertEquals("PRE_PAYMENT", fees.get(1).get("type"));
        assertEquals(542, fees.get(1).get("amount"));

        List<Map<String, Object>> events = parser.objectToList(capture.get("events"));

        // events[0]
        assertEquals("PAYMENT.AUTHORIZED", events.get(0).get("type"));
        assertEquals("2018-07-16T15:51:49.583-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("PAYMENT.IN_ANALYSIS", events.get(1).get("type"));
        assertEquals("2018-07-16T14:11:40.000-03", events.get(1).get("createdAt"));

        // events[2]
        assertEquals("PAYMENT.PRE_AUTHORIZED", events.get(2).get("type"));
        assertEquals("2018-07-16T14:11:40.000-03", events.get(2).get("createdAt"));

        // events[3]
        assertEquals("PAYMENT.CREATED", events.get(3).get("type"));
        assertEquals("2018-07-16T14:11:34.000-03", events.get(3).get("createdAt"));

        List<Map<String, Object>> receivers = parser.objectToList(capture.get("receivers"));
        Map<String, Object> moipAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccount.get("id"));
        assertEquals("integracao@labs.moip.com.br", moipAccount.get("login"));
        assertEquals("Moip SandBox", moipAccount.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> amountReceived = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(11000, amountReceived.get("total"));
        assertEquals("BRL", amountReceived.get("currency"));
        assertEquals(1127, amountReceived.get("fees"));
        assertEquals(0, amountReceived.get("refunds"));

        assertEquals(true, receivers.get(0).get("feePayor"));

        Map<String, Object> links = parser.objectToMap(capture.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-MMU64JJBUI6Z", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-J5F0JITE58DV", order.get("href"));
        assertEquals("ORD-J5F0JITE58DV", order.get("title"));

        assertEquals("2018-07-16T14:11:34.000-03", capture.get("createdAt"));
        assertEquals("2018-07-16T15:51:49.582-03", capture.get("updatedAt"));
    }

    @Play("payment/cancel_pre_authorized")
    @Test
    public void cancelPreAuthorized() {

        Map<String, Object> cancel = Moip.API.payments().cancelPreAuthorized("PAY-44R4XT17VP50", setup);

        assertEquals("PAY-44R4XT17VP50", cancel.get("id"));
        assertEquals("CANCELLED", cancel.get("status"));
        assertEquals(true, cancel.get("delayCapture"));

        Map<String, Object> cancellationDetails = parser.objectToMap(cancel.get("cancellationDetails"));
        assertEquals("7", cancellationDetails.get("code"));
        assertEquals("Política do Moip", cancellationDetails.get("description"));
        assertEquals("MOIP", cancellationDetails.get("cancelledBy"));

        Map<String, Object> amount = parser.objectToMap(cancel.get("amount"));
        assertEquals(11000, amount.get("total"));
        assertEquals(11000, amount.get("gross"));
        assertEquals(1127, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(9873, amount.get("liquid"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(3, cancel.get("installmentCount"));
        assertEquals("Minha Loja", cancel.get("statementDescriptor"));

        Map<String, Object> fundingInstrument = parser.objectToMap(cancel.get("fundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-CQC2YNRA6UUT", creditCard.get("id"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        Map<String, Object> holder = parser.objectToMap(creditCard.get("holder"));
        assertEquals("1988-12-30", holder.get("birthDate"));

        Map<String, Object> taxDocumentHolder = parser.objectToMap(holder.get("taxDocument"));
        assertEquals("CPF", taxDocumentHolder.get("type"));
        assertEquals("78994193600", taxDocumentHolder.get("number"));

        Map<String, Object> billingAddress = parser.objectToMap(holder.get("billingAddress"));
        assertEquals("Avenida Contorno", billingAddress.get("street"));
        assertEquals("400", billingAddress.get("streetNumber"));
        assertEquals("Savassi", billingAddress.get("district"));
        assertEquals("Belo Horizonte", billingAddress.get("city"));
        assertEquals("MG", billingAddress.get("state"));
        assertEquals("BRA", billingAddress.get("country"));
        assertEquals("76932800", billingAddress.get("zipCode"));

        assertEquals("Roberto Oliveira", holder.get("fullname"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> acquirerDetails = parser.objectToMap(cancel.get("acquirerDetails"));
        assertEquals("123456", acquirerDetails.get("authorizationNumber"));

        Map<String, Object> taxDocument = parser.objectToMap(acquirerDetails.get("taxDocument"));
        assertEquals("CNPJ", taxDocument.get("type"));
        assertEquals("01027058000191", taxDocument.get("number"));

        List<Map<String, Object>> fees = parser.objectToList(cancel.get("fees"));

        // fees[0]
        assertEquals("TRANSACTION", fees.get(0).get("type"));
        assertEquals(585, fees.get(0).get("amount"));

        // fees[1]
        assertEquals("PRE_PAYMENT", fees.get(1).get("type"));
        assertEquals(542, fees.get(1).get("amount"));

        List<Map<String, Object>> events = parser.objectToList(cancel.get("events"));

        // events[0]
        assertEquals("PAYMENT.CANCELLED", events.get(0).get("type"));
        assertEquals("2018-07-16T16:12:26.499-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("PAYMENT.PRE_AUTHORIZED", events.get(1).get("type"));
        assertEquals("2018-07-16T16:12:12.000-03", events.get(1).get("createdAt"));

        // events[2]
        assertEquals("PAYMENT.IN_ANALYSIS", events.get(2).get("type"));
        assertEquals("2018-07-16T16:12:11.000-03", events.get(2).get("createdAt"));

        // events[3]
        assertEquals("PAYMENT.CREATED", events.get(3).get("type"));
        assertEquals("2018-07-16T16:11:50.000-03", events.get(3).get("createdAt"));

        List<Map<String, Object>> receivers = parser.objectToList(cancel.get("receivers"));
        Map<String, Object> moipAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccount.get("id"));
        assertEquals("integracao@labs.moip.com.br", moipAccount.get("login"));
        assertEquals("Moip SandBox", moipAccount.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> amountReceived = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(11000, amountReceived.get("total"));
        assertEquals("BRL", amountReceived.get("currency"));
        assertEquals(0, amountReceived.get("fees"));
        assertEquals(0, amountReceived.get("refunds"));

        assertEquals(true, receivers.get(0).get("feePayor"));

        Map<String, Object> links = parser.objectToMap(cancel.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-44R4XT17VP50", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-3Y3NR4PS4HVP", order.get("href"));
        assertEquals("ORD-3Y3NR4PS4HVP", order.get("title"));

        assertEquals("2018-07-16T16:11:50.000-03", cancel.get("createdAt"));
        assertEquals("2018-07-16T16:12:26.498-03", cancel.get("updatedAt"));
    }

    @Play("payment/get")
    @Test
    public void getPaymentTest() {

        Map<String, Object> payment = Moip.API.payments().get("PAY-MMU64JJBUI6Z", setup);

        assertEquals("PAY-MMU64JJBUI6Z", payment.get("id"));
        assertEquals("AUTHORIZED", payment.get("status"));
        assertEquals(true, payment.get("delayCapture"));

        Map<String, Object> amount = parser.objectToMap(payment.get("amount"));
        assertEquals(11000, amount.get("total"));
        assertEquals(11000, amount.get("gross"));
        assertEquals(1127, amount.get("fees"));
        assertEquals(0, amount.get("refunds"));
        assertEquals(9873, amount.get("liquid"));
        assertEquals("BRL", amount.get("currency"));

        assertEquals(3, payment.get("installmentCount"));
        assertEquals("Minha Loja", payment.get("statementDescriptor"));

        Map<String, Object> fundingInstrument = parser.objectToMap(payment.get("fundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(fundingInstrument.get("creditCard"));
        assertEquals("CRC-CQC2YNRA6UUT", creditCard.get("id"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        Map<String, Object> holder = parser.objectToMap(creditCard.get("holder"));
        assertEquals("1988-12-30", holder.get("birthDate"));

        Map<String, Object> taxDocumentHolder = parser.objectToMap(holder.get("taxDocument"));
        assertEquals("CPF", taxDocumentHolder.get("type"));
        assertEquals("78994193600", taxDocumentHolder.get("number"));

        Map<String, Object> billingAddress = parser.objectToMap(holder.get("billingAddress"));
        assertEquals("Avenida Contorno", billingAddress.get("street"));
        assertEquals("400", billingAddress.get("streetNumber"));
        assertEquals("Savassi", billingAddress.get("district"));
        assertEquals("Belo Horizonte", billingAddress.get("city"));
        assertEquals("MG", billingAddress.get("state"));
        assertEquals("BRA", billingAddress.get("country"));
        assertEquals("76932800", billingAddress.get("zipCode"));

        assertEquals("Roberto Oliveira", holder.get("fullname"));

        assertEquals("CREDIT_CARD", fundingInstrument.get("method"));

        Map<String, Object> acquirerDetails = parser.objectToMap(payment.get("acquirerDetails"));
        assertEquals("123456", acquirerDetails.get("authorizationNumber"));

        Map<String, Object> taxDocument = parser.objectToMap(acquirerDetails.get("taxDocument"));
        assertEquals("CNPJ", taxDocument.get("type"));
        assertEquals("01027058000191", taxDocument.get("number"));

        List<Map<String, Object>> fees = parser.objectToList(payment.get("fees"));

        // fees[0]
        assertEquals("TRANSACTION", fees.get(0).get("type"));
        assertEquals(585, fees.get(0).get("amount"));

        // fees[1]
        assertEquals("PRE_PAYMENT", fees.get(1).get("type"));
        assertEquals(542, fees.get(1).get("amount"));

        List<Map<String, Object>> events = parser.objectToList(payment.get("events"));

        // events[0]
        assertEquals("PAYMENT.AUTHORIZED", events.get(0).get("type"));
        assertEquals("2018-07-16T15:51:49.000-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("PAYMENT.IN_ANALYSIS", events.get(1).get("type"));
        assertEquals("2018-07-16T14:11:40.000-03", events.get(1).get("createdAt"));

        // events[2]
        assertEquals("PAYMENT.PRE_AUTHORIZED", events.get(2).get("type"));
        assertEquals("2018-07-16T14:11:40.000-03", events.get(2).get("createdAt"));

        // events[3]
        assertEquals("PAYMENT.CREATED", events.get(3).get("type"));
        assertEquals("2018-07-16T14:11:34.000-03", events.get(3).get("createdAt"));

        List<Map<String, Object>> receivers = parser.objectToList(payment.get("receivers"));
        Map<String, Object> moipAccount = parser.objectToMap(receivers.get(0).get("moipAccount"));
        assertEquals("MPA-CULBBYHD11", moipAccount.get("id"));
        assertEquals("integracao@labs.moip.com.br", moipAccount.get("login"));
        assertEquals("Moip SandBox", moipAccount.get("fullname"));

        assertEquals("PRIMARY", receivers.get(0).get("type"));

        Map<String, Object> amountReceived = parser.objectToMap(receivers.get(0).get("amount"));
        assertEquals(11000, amountReceived.get("total"));
        assertEquals("BRL", amountReceived.get("currency"));
        assertEquals(1127, amountReceived.get("fees"));
        assertEquals(0, amountReceived.get("refunds"));

        assertEquals(true, receivers.get(0).get("feePayor"));

        Map<String, Object> links = parser.objectToMap(payment.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-MMU64JJBUI6Z", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-J5F0JITE58DV", order.get("href"));
        assertEquals("ORD-J5F0JITE58DV", order.get("title"));

        assertEquals("2018-07-16T14:11:34.000-03", payment.get("createdAt"));
        assertEquals("2018-07-16T15:51:49.000-03", payment.get("updatedAt"));
    }
}
