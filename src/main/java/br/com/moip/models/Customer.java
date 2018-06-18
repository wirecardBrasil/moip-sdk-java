package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class Customer {

    private static final String ENDPOINT = "/v2/customers";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    /**
     * This method is used to create an customer.
     *
     * @param   body
     *          {@code Map<String,Object>} the map charged with the correct request
     *          attributes.
     *
     * @param   setup
     *          {@code Setup} the basic connection setup (authentication and timeouts).
     *
     * @return  {@code Map}
     */
    public Map<String, Object> create(Map<String, Object> body, Setup setup) {
        requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(ENDPOINT)
                .body(body)
                .type(Customer.class)
                .contentType(CONTENT_TYPE);

        return requestMaker.doRequest(props);
    }

    public Map<String, Object> get(String moipId, Setup setup) {
        requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format("%s/%s", ENDPOINT, moipId))
                .type(Customer.class)
                .contentType(CONTENT_TYPE)
                .build();

        return requestMaker.doRequest(props);
    }
}
