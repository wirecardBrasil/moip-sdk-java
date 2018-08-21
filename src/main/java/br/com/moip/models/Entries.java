package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class Entries {

    private static final String ENDPOINT = "/v2/entries";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    /**
     * This method allows you to get the entry data by its Moip external ID.
     *
     * @param   entryId
     *          {@code String} the Moip entry external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> get(String entryId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format("%s/%s", ENDPOINT, entryId))
                .type(Entries.class)
                .contentType(CONTENT_TYPE)
                .accept("2.1")
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to list all entries.
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
                .type(Entries.class)
                .contentType(CONTENT_TYPE)
                .accept("2.1")
                .build();

        return this.requestMaker.doRequest(props);
    }
}
