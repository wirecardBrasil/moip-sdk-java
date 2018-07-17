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

public class RefundTest {

    @Rule
    public Player player = new Player();
    private Setup setup;
    private Map<String, Object> body;
    private Parser parser;

    @Before
    public void initialize() {
        this.body = new HashMap<>();
        this.setup = new SetupFactory().setup(player.getURL("").toString());
        this.parser = new Parser();
    }

    @Play("refund/full_refund_payment")
    @Test
    public void fullRefundPaymentTest() {

        Map<String, Object> refund = Moip.API.refunds().refundPayment("PAY-MMU64JJBUI6Z", setup);

        assertEquals("REF-DBM34ELHHHS6", refund.get("id"));
        assertEquals("COMPLETED", refund.get("status"));

        List<Map<String, Object>> events = parser.objectToList(refund.get("events"));

        // events[0]
        assertEquals("REFUND.COMPLETED", events.get(0).get("type"));
        assertEquals("2018-07-17T14:13:22.000-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("REFUND.REQUESTED", events.get(1).get("type"));
        assertEquals("2018-07-17T14:13:22.000-03", events.get(1).get("createdAt"));

        Map<String, Object> amount = parser.objectToMap(refund.get("amount"));
        assertEquals(11000, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals("BRL", amount.get("currency"));

        List<Map<String, Object>> receiversDebited = parser.objectToList(refund.get("receiversDebited"));
        assertEquals(9873, receiversDebited.get(0).get("amount"));
        assertEquals("MPA-CULBBYHD11", receiversDebited.get(0).get("moipAccount"));

        assertEquals("FULL", refund.get("type"));

        Map<String, Object> refundingInstrument = parser.objectToMap(refund.get("refundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(refundingInstrument.get("creditCard"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", refundingInstrument.get("method"));

        assertEquals("2018-07-17T14:13:22.000-03", refund.get("createdAt"));

        Map<String, Object> links = parser.objectToMap(refund.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/refunds/REF-DBM34ELHHHS6", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-J5F0JITE58DV", order.get("href"));
        assertEquals("ORD-J5F0JITE58DV", order.get("title"));

        Map<String, Object> payment = parser.objectToMap(links.get("payment"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-MMU64JJBUI6Z", payment.get("href"));
        assertEquals("PAY-MMU64JJBUI6Z", payment.get("title"));
    }

    @Play("refund/full_refund_order")
    @Test
    public void fullRefundOrderTest() {

        Map<String, Object> refund = Moip.API.refunds().refundOrder("ORD-BCLUATUQ8K4W", setup);

        assertEquals("REF-MYLT39W1ZWEU", refund.get("id"));
        assertEquals("COMPLETED", refund.get("status"));

        List<Map<String, Object>> events = parser.objectToList(refund.get("events"));

        // events[0]
        assertEquals("REFUND.COMPLETED", events.get(0).get("type"));
        assertEquals("2018-07-17T16:16:16.000-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("REFUND.REQUESTED", events.get(1).get("type"));
        assertEquals("2018-07-17T16:16:16.000-03", events.get(1).get("createdAt"));

        Map<String, Object> amount = parser.objectToMap(refund.get("amount"));
        assertEquals(11000, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals("BRL", amount.get("currency"));

        List<Map<String, Object>> receiversDebited = parser.objectToList(refund.get("receiversDebited"));
        assertEquals(10154, receiversDebited.get(0).get("amount"));
        assertEquals("MPA-CULBBYHD11", receiversDebited.get(0).get("moipAccount"));

        assertEquals("FULL", refund.get("type"));

        Map<String, Object> refundingInstrument = parser.objectToMap(refund.get("refundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(refundingInstrument.get("creditCard"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", refundingInstrument.get("method"));

        assertEquals("2018-07-17T16:16:16.000-03", refund.get("createdAt"));

        Map<String, Object> links = parser.objectToMap(refund.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/refunds/REF-MYLT39W1ZWEU", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-BCLUATUQ8K4W", order.get("href"));
        assertEquals("ORD-BCLUATUQ8K4W", order.get("title"));

        Map<String, Object> payment = parser.objectToMap(links.get("payment"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-FE5GR3YXP151", payment.get("href"));
        assertEquals("PAY-FE5GR3YXP151", payment.get("title"));
    }

    @Play("refund/partial_refund_payment")
    @Test
    public void partialRefundPaymentTest() {

        Map<String, Object> refund = Moip.API.refunds().refundPayment(body, "PAY-SB2B44QAXILI", setup);

        assertEquals("REF-1QXB9GKQJRNF", refund.get("id"));
        assertEquals("COMPLETED", refund.get("status"));

        List<Map<String, Object>> events = parser.objectToList(refund.get("events"));

        // events[0]
        assertEquals("REFUND.COMPLETED", events.get(0).get("type"));
        assertEquals("2018-07-17T16:26:09.000-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("REFUND.REQUESTED", events.get(1).get("type"));
        assertEquals("2018-07-17T16:26:09.000-03", events.get(1).get("createdAt"));

        Map<String, Object> amount = parser.objectToMap(refund.get("amount"));
        assertEquals(8500, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals("BRL", amount.get("currency"));

        List<Map<String, Object>> receiversDebited = parser.objectToList(refund.get("receiversDebited"));
        assertEquals(8500, receiversDebited.get(0).get("amount"));
        assertEquals("MPA-CULBBYHD11", receiversDebited.get(0).get("moipAccount"));

        assertEquals("PARTIAL", refund.get("type"));

        Map<String, Object> refundingInstrument = parser.objectToMap(refund.get("refundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(refundingInstrument.get("creditCard"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", refundingInstrument.get("method"));

        assertEquals("2018-07-17T16:26:09.000-03", refund.get("createdAt"));

        Map<String, Object> links = parser.objectToMap(refund.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/refunds/REF-1QXB9GKQJRNF", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-FF9UACPDT5N6", order.get("href"));
        assertEquals("ORD-FF9UACPDT5N6", order.get("title"));

        Map<String, Object> payment = parser.objectToMap(links.get("payment"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-SB2B44QAXILI", payment.get("href"));
        assertEquals("PAY-SB2B44QAXILI", payment.get("title"));
    }

    @Play("refund/partial_refund_order")
    @Test
    public void partialRefundOrderTest() {

        Map<String, Object> refund = Moip.API.refunds().refundOrder(body, "ORD-PCSMIB5VZWGN", setup);

        assertEquals("REF-ZRNB6YXA3DVN", refund.get("id"));
        assertEquals("COMPLETED", refund.get("status"));

        List<Map<String, Object>> events = parser.objectToList(refund.get("events"));

        // events[0]
        assertEquals("REFUND.COMPLETED", events.get(0).get("type"));
        assertEquals("2018-07-17T16:36:07.000-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("REFUND.REQUESTED", events.get(1).get("type"));
        assertEquals("2018-07-17T16:36:06.000-03", events.get(1).get("createdAt"));

        Map<String, Object> amount = parser.objectToMap(refund.get("amount"));
        assertEquals(8500, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals("BRL", amount.get("currency"));

        List<Map<String, Object>> receiversDebited = parser.objectToList(refund.get("receiversDebited"));
        assertEquals(8500, receiversDebited.get(0).get("amount"));
        assertEquals("MPA-CULBBYHD11", receiversDebited.get(0).get("moipAccount"));

        assertEquals("PARTIAL", refund.get("type"));

        Map<String, Object> refundingInstrument = parser.objectToMap(refund.get("refundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(refundingInstrument.get("creditCard"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", refundingInstrument.get("method"));

        assertEquals("2018-07-17T16:36:06.000-03", refund.get("createdAt"));

        Map<String, Object> links = parser.objectToMap(refund.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/refunds/REF-ZRNB6YXA3DVN", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-PCSMIB5VZWGN", order.get("href"));
        assertEquals("ORD-PCSMIB5VZWGN", order.get("title"));

        Map<String, Object> payment = parser.objectToMap(links.get("payment"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-NG0P224NUIWR", payment.get("href"));
        assertEquals("PAY-NG0P224NUIWR", payment.get("title"));
    }

    @Play("refund/get")
    @Test
    public void getRefundTest() {

        Map<String, Object> refund = Moip.API.refunds().get("REF-ZRNB6YXA3DVN", setup);

        assertEquals("REF-ZRNB6YXA3DVN", refund.get("id"));
        assertEquals("COMPLETED", refund.get("status"));

        List<Map<String, Object>> events = parser.objectToList(refund.get("events"));

        // events[0]
        assertEquals("REFUND.COMPLETED", events.get(0).get("type"));
        assertEquals("2018-07-17T16:36:07.000-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("REFUND.REQUESTED", events.get(1).get("type"));
        assertEquals("2018-07-17T16:36:06.000-03", events.get(1).get("createdAt"));

        Map<String, Object> amount = parser.objectToMap(refund.get("amount"));
        assertEquals(8500, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals("BRL", amount.get("currency"));

        List<Map<String, Object>> receiversDebited = parser.objectToList(refund.get("receiversDebited"));
        assertEquals(8500, receiversDebited.get(0).get("amount"));
        assertEquals("MPA-CULBBYHD11", receiversDebited.get(0).get("moipAccount"));

        assertEquals("PARTIAL", refund.get("type"));

        Map<String, Object> refundingInstrument = parser.objectToMap(refund.get("refundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(refundingInstrument.get("creditCard"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", refundingInstrument.get("method"));

        assertEquals("2018-07-17T16:36:06.000-03", refund.get("createdAt"));

        Map<String, Object> links = parser.objectToMap(refund.get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/refunds/REF-ZRNB6YXA3DVN", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-PCSMIB5VZWGN", order.get("href"));
        assertEquals("ORD-PCSMIB5VZWGN", order.get("title"));

        Map<String, Object> payment = parser.objectToMap(links.get("payment"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-NG0P224NUIWR", payment.get("href"));
        assertEquals("PAY-NG0P224NUIWR", payment.get("title"));
    }

    @Play("refund/list_payment_refunds")
    @Test
    public void listPaymentRefundsTest() {

        Map<String, Object> listRefunds = Moip.API.refunds().listPaymentRefunds("PAY-NG0P224NUIWR", setup);

        List<Map<String, Object>> refunds = parser.objectToList(listRefunds.get("refunds"));

        assertEquals("REF-ZRNB6YXA3DVN", refunds.get(0).get("id"));
        assertEquals("COMPLETED", refunds.get(0).get("status"));

        List<Map<String, Object>> events = parser.objectToList(refunds.get(0).get("events"));

        // events[0]
        assertEquals("REFUND.COMPLETED", events.get(0).get("type"));
        assertEquals("2018-07-17T16:36:07.000-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("REFUND.REQUESTED", events.get(1).get("type"));
        assertEquals("2018-07-17T16:36:06.000-03", events.get(1).get("createdAt"));

        Map<String, Object> amount = parser.objectToMap(refunds.get(0).get("amount"));
        assertEquals(8500, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals("BRL", amount.get("currency"));

        List<Map<String, Object>> receiversDebited = parser.objectToList(refunds.get(0).get("receiversDebited"));
        assertEquals(8500, receiversDebited.get(0).get("amount"));
        assertEquals("MPA-CULBBYHD11", receiversDebited.get(0).get("moipAccount"));

        assertEquals("PARTIAL", refunds.get(0).get("type"));

        Map<String, Object> refundingInstrument = parser.objectToMap(refunds.get(0).get("refundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(refundingInstrument.get("creditCard"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", refundingInstrument.get("method"));

        assertEquals("2018-07-17T16:36:06.000-03", refunds.get(0).get("createdAt"));

        Map<String, Object> links = parser.objectToMap(refunds.get(0).get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/refunds/REF-ZRNB6YXA3DVN", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-PCSMIB5VZWGN", order.get("href"));
        assertEquals("ORD-PCSMIB5VZWGN", order.get("title"));

        Map<String, Object> payment = parser.objectToMap(links.get("payment"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-NG0P224NUIWR", payment.get("href"));
        assertEquals("PAY-NG0P224NUIWR", payment.get("title"));
    }

    @Play("refund/list_order_refunds")
    @Test
    public void listOrderRefundsTest() {

        Map<String, Object> listRefunds = Moip.API.refunds().listOrderRefunds("ORD-PCSMIB5VZWGN", setup);

        List<Map<String, Object>> refunds = parser.objectToList(listRefunds.get("refunds"));

        assertEquals("REF-ZRNB6YXA3DVN", refunds.get(0).get("id"));
        assertEquals("COMPLETED", refunds.get(0).get("status"));

        List<Map<String, Object>> events = parser.objectToList(refunds.get(0).get("events"));

        // events[0]
        assertEquals("REFUND.COMPLETED", events.get(0).get("type"));
        assertEquals("2018-07-17T16:36:07.000-03", events.get(0).get("createdAt"));

        // events[1]
        assertEquals("REFUND.REQUESTED", events.get(1).get("type"));
        assertEquals("2018-07-17T16:36:06.000-03", events.get(1).get("createdAt"));

        Map<String, Object> amount = parser.objectToMap(refunds.get(0).get("amount"));
        assertEquals(8500, amount.get("total"));
        assertEquals(0, amount.get("fees"));
        assertEquals("BRL", amount.get("currency"));

        List<Map<String, Object>> receiversDebited = parser.objectToList(refunds.get(0).get("receiversDebited"));
        assertEquals(8500, receiversDebited.get(0).get("amount"));
        assertEquals("MPA-CULBBYHD11", receiversDebited.get(0).get("moipAccount"));

        assertEquals("PARTIAL", refunds.get(0).get("type"));

        Map<String, Object> refundingInstrument = parser.objectToMap(refunds.get(0).get("refundingInstrument"));
        Map<String, Object> creditCard = parser.objectToMap(refundingInstrument.get("creditCard"));
        assertEquals("MASTERCARD", creditCard.get("brand"));
        assertEquals("555566", creditCard.get("first6"));
        assertEquals("8884", creditCard.get("last4"));
        assertEquals(true, creditCard.get("store"));

        assertEquals("CREDIT_CARD", refundingInstrument.get("method"));

        assertEquals("2018-07-17T16:36:06.000-03", refunds.get(0).get("createdAt"));

        Map<String, Object> links = parser.objectToMap(refunds.get(0).get("_links"));
        Map<String, Object> self = parser.objectToMap(links.get("self"));
        assertEquals("https://sandbox.moip.com.br/v2/refunds/REF-ZRNB6YXA3DVN", self.get("href"));

        Map<String, Object> order = parser.objectToMap(links.get("order"));
        assertEquals("https://sandbox.moip.com.br/v2/orders/ORD-PCSMIB5VZWGN", order.get("href"));
        assertEquals("ORD-PCSMIB5VZWGN", order.get("title"));

        Map<String, Object> payment = parser.objectToMap(links.get("payment"));
        assertEquals("https://sandbox.moip.com.br/v2/payments/PAY-NG0P224NUIWR", payment.get("href"));
        assertEquals("PAY-NG0P224NUIWR", payment.get("title"));
    }
}
