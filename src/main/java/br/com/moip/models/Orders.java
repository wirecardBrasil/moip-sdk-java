package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class Orders {

    private static final String ENDPOINT = "/v2/orders";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    /**
     * This method is used to create a new order.
     *
     * @param   body
     *          {@code Map<String, Object>} the request body.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> create(Map<String, Object> body, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(ENDPOINT)
                .body(body)
                .type(Orders.class)
                .contentType(CONTENT_TYPE)
                .build();

        return requestMaker.doRequest(props);
    }

    /**
     * This method is used to get the data of a created order by Moip order external ID.
     *
     * @param   orderId
     *          {@code String} the Moip order external ID. Ex: ORD-XXXXXXXXXXXX
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> get(String orderId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format("%s/%s", ENDPOINT, orderId))
                .type(Orders.class)
                .contentType(CONTENT_TYPE)
                .build();

        return requestMaker.doRequest(props);
    }

    /**
     * This method is used to get all created orders.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> list(Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(ENDPOINT)
                .type(Orders.class)
                .contentType(CONTENT_TYPE)
                .build();

        return requestMaker.doRequest(props);
    }

    /**
     * This method is used to get all payments of an order by its Moip order external ID.
     *
     * @param   orderId
     *          {@code String} the Moip order external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> listOrderPayments(String orderId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format("%s/%s/payments", ENDPOINT, orderId))
                .type(Orders.class)
                .contentType(CONTENT_TYPE)
                .build();

        return requestMaker.doRequest(props);
    }
}
