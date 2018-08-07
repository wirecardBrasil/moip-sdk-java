package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class Refunds {

    private static final String ENDPOINT = "/v2/refunds/%s";
    private static final String ENDPOINT_REFUND_ORDER = "/v2/orders/%s/refunds";
    private static final String ENDPOINT_REFUND_PAYMENT = "/v2/payments/%s/refunds";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    /**
     * This method is used to create a payment refund without request body.
     * TIP: Actually, its possible make only refund by credit card with this method.
     *
     * @param   paymentId
     *          {@code String} the Moip payment external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> refundPayment(String paymentId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(String.format(ENDPOINT_REFUND_PAYMENT, paymentId))
                .type(Refunds.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to create a payment refund with request body.
     *
     * @param   body
     *          {@code Map<String, Object>} the map charged with request attributes.
     *
     * @param   paymentId
     *          {@code String} the Moip payment external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> refundPayment(Map<String, Object> body, String paymentId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(String.format(ENDPOINT_REFUND_PAYMENT, paymentId))
                .body(body)
                .type(Refunds.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to create a order refund without request body.
     * TIP: Actually, its possible make only refund by credit card with this method.
     *
     * @param   orderId
     *          {@code String} the Moip order external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> refundOrder(String orderId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(String.format(ENDPOINT_REFUND_ORDER, orderId))
                .type(Refunds.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to create a order refund with request body.
     *
     * @param   body
     *          {@code Map<String, Object>} the map charged with request attributes.
     *
     * @param   orderId
     *          {@code String} the Moip order external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> refundOrder(Map<String, Object> body, String orderId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(String.format(ENDPOINT_REFUND_ORDER, orderId))
                .body(body)
                .type(Refunds.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to get the data of a created refund by Moip refund external ID.
     *
     * @param   refundId
     *          {@code String} the Moip refund external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> get(String refundId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format(ENDPOINT, refundId))
                .type(Refunds.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to get all refunds of an payment by Moip payment external ID.
     *
     * @param   paymentId
     *          {@code String} the Moip payment external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> listPaymentRefunds(String paymentId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format(ENDPOINT_REFUND_PAYMENT, paymentId))
                .type(Refunds.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to get all refunds of an order by Moip order external ID.
     *
     * @param   orderId
     *          {@code String} the Moip order external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> listOrderRefunds(String orderId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format(ENDPOINT_REFUND_ORDER, orderId))
                .type(Refunds.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }
}
