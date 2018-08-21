package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class Multiorders {

    private static final String ENDPOINT = "/v2/multiorders";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    /**
     * This method allows you to create a multiorder. The multiorder is a collection of orders, its used to
     * transact with different sellers in the same shop cart.
     *
     * @param   body
     *          {@code Map<String, Object} the request body.
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
                .type(Multiorders.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to create a multipayment, it means create a payment for each order from a multiorder
     * with the same funding instrument (credit card, boleto, online bank debit).
     *
     * @param   body
     *          {@code Map<String, Object>} the request body.
     *
     * @param   multiorderId
     *          {@code String} the Moip multiorder external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> pay(Map<String, Object> body, String multiorderId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(String.format("%s/%s/multipayments", ENDPOINT, multiorderId))
                .body(body)
                .type(Multipayments.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to get a created multiorder by Moip multiorder external ID.
     *
     * @param   id
     *          {@code String} the Moip multiorder external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> get(String id, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format("%s/%s", ENDPOINT, id))
                .type(Multiorders.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }
}
