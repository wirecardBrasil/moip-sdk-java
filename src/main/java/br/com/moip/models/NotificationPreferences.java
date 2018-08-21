package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.List;
import java.util.Map;

public class NotificationPreferences {

    private static final String ENDPOINT = "/v2/preferences/notifications";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    /**
     * This method is used to create a notification preference to receive Webhooks.
     *
     * @param   body
     *          {@code Map<String, Object>} the map charged with request attributes.
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
                .type(NotificationPreferences.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to get the data of a created notification preference.
     *
     * @param   notificationPreferenceId
     *          {@code String} the Moip notification preference external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> get(String notificationPreferenceId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format("%s/%s", ENDPOINT, notificationPreferenceId))
                .type(NotificationPreferences.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method is used to get all created notification preferences.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code List<Map<String, Object>>}
     */
    public List<Map<String, Object>> list(Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(ENDPOINT)
                .type(NotificationPreferences.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.getList(props);
    }

    /**
     * This method is used to remove a created notification preference.
     *
     * @param   notificationPreferenceId
     *          {@code String} the Moip notification preference external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> remove(String notificationPreferenceId, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("DELETE")
                .endpoint(String.format("%s/%s", ENDPOINT, notificationPreferenceId))
                .type(NotificationPreferences.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }
}
