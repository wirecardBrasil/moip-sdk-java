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
     *
     * @param body
     * @param orderId
     * @param setup
     * @return
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
     *
     * @param paymentId
     * @param setup
     * @return
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
