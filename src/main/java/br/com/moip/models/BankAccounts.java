package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class BankAccounts {

    private static final String ENDPOINT = "/v2/bankaccounts";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    /**
     * This method allows you to get the data of a created bank account by its Moip external ID.
     *
     * @param   bankAccountId
     *          {@code String} the Moip bank account external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> get(String bankAccountId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format("%s/%s", ENDPOINT, bankAccountId))
                .type(BankAccounts.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method allows you to update a created bank account. To make it, you have to send only the attributes
     * that you want to change with its new values. For example, if you want to change the agencyNumber 12345 to
     * 54321 and the agencyCheckNumber 4 to 6:
     * {@code "agencyNumber": "54321"}
     * {@code "agencyCheckNumber": "6"}
     *
     * @param   body
     *          {@code Map<String, Object} the request body.
     *
     * @param   bankAccountId
     *          {@code String} the Moip bank account external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> update(Map<String, Object> body, String bankAccountId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("PUT")
                .endpoint(String.format("%s/%s", ENDPOINT, bankAccountId))
                .body(body)
                .type(BankAccounts.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method allows you to delete a created bank account by its Moip external ID.
     *
     * @param   bankAccountId
     *          {@code String} the Moip bank account external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> delete(String bankAccountId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("DELETE")
                .endpoint(String.format("%s/%s", ENDPOINT, bankAccountId))
                .type(BankAccounts.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }
}
