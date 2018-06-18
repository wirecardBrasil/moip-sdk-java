package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class Payment {

    private static final String ENDPOINT = "/v2/payments";
    private static final String ENDPOINT_TO_PAY = "/v2/orders/%s/payments";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    /**
     * This method is used to create a payment. To make it, is necessary send the {@code body}
     * filled with the payment data and the ID of the order that will be payed.
     *
     * @param   body
     *          {@code Map<String, Object>} the request body.
     *
     * @param   orderId
     *          {@code String} the Moip order external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> pay(Map<String, Object> body, String orderId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(String.format(ENDPOINT_TO_PAY, orderId))
                .body(body)
                .type(Payment.class)
                .contentType(CONTENT_TYPE)
                .build();

        return requestMaker.doRequest(props);
    }

    /**
     * This method is used to get the data of a created payment by Moip payment external ID.
     *
     * @param   paymentId
     *          {@code String} the Moip payment external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> get(String paymentId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format("%s/%s", ENDPOINT, paymentId))
                .type(Payment.class)
                .contentType(CONTENT_TYPE)
                .build();

        return requestMaker.doRequest(props);
    }
}
