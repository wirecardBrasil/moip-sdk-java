package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import br.com.moip.exception.ValidationException;
import br.com.moip.models.error.Errors;
import br.com.moip.models.error.ErrorBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

import static br.com.moip.Moip.SANDBOX_URL;

public class Payments {

    private static final String ENDPOINT = "/v2/payments";
    private static final String ENDPOINT_TO_PAY = "/v2/orders/%s/payments";
    private static final String ENDPOINT_TO_AUTHORIZE = "/simulador/authorize";
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
                .type(Payments.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to capture a pre-authorized payment by its Moip payment external ID.
     *
     * @param   paymentId
     *          {@code String} the Moip payment external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> capturePreAuthorized(String paymentId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(String.format("%s/%s/capture", ENDPOINT, paymentId))
                .type(Payments.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to cancel a pre-authorized payment by Moip payment external ID.
     *
     * @param   paymentId
     *          {@code String} the Moip payment external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> cancelPreAuthorized(String paymentId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(String.format("%s/%s/void", ENDPOINT, paymentId))
                .type(Payments.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
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
                .type(Payments.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to simulate the payment authorization. To make it, its necessary
     * send the Moip payment external ID and the amount value.
     *
     * WARNING: This method can be used to request only Sandbox environment.
     *
     * @param   paymentId
     *          {@code String} the Moip payment external ID.
     *
     * @param   amount
     *          {@code int} the amount value that will be authorized.
     *
     * @param   setup
     *          {@code Setup} the setup obeject.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> authorize(String paymentId, int amount, Setup setup) {
        if (setup.getEnvironment() == SANDBOX_URL) {
            this.requestMaker = new RequestMaker(setup);
            RequestProperties props = new RequestPropertiesBuilder()
                    .method("GET")
                    .endpoint(String.format("%s?payment_id=%s&amount=%s", ENDPOINT_TO_AUTHORIZE, paymentId, amount))
                    .type(Payments.class)
                    .contentType(CONTENT_TYPE)
                    .build();

            return this.requestMaker.doRequest(props);
        }
        else {
            ErrorBuilder error = new ErrorBuilder()
                    .code("404")
                    .path("")
                    .description("Wrong environment! Only payments created on Sandbox environment can be manually authorized.");

            Errors errors = new Errors();
            errors.setError(error);

            throw new ValidationException(404, "Not Found", errors);
        }
    }
}
