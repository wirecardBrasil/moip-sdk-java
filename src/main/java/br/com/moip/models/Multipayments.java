package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class Multipayments {

    private static final String ENDPOINT = "/v2/multipayments";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    /**
     * This method is used to capture a pre-authorized multipayment by its Moip multipayment external ID.
     *
     * @param   multipaymentId
     *          {@code String} the Moip multipayment external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> capturePreAuthorized(String multipaymentId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(String.format("%s/%s/capture", ENDPOINT, multipaymentId))
                .type(Multipayments.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to cancel a pre-authorized multipayment by its Moip multipayment external ID.
     *
     * @param   multipaymentId
     *          {@code String} the Moip multipayment external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> cancelPreAuthorized(String multipaymentId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(String.format("%s/%s/void", ENDPOINT, multipaymentId))
                .type(Multipayments.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method allows you to get the data of a created multipayment by its Moip multipayment external ID.
     *
     * @param   multipaymentId
     *          {@code String} the Moip multipayment external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> get(String multipaymentId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format("%s/%s", ENDPOINT, multipaymentId))
                .type(Multipayments.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }
}
