package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class Connect {

    private static final String ENDPOINT = "/oauth/token";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_FORM_URLENCODED;
    private RequestMaker requestMaker;

    /**
     * This method is used to build the URL to request access permission for your seller.
     *
     * @param   clientId
     *          {@code String} the APP ID.
     *
     * @param   redirectUri
     *          {@code String} the address that you want to redirect your user after they grant the permission.
     *
     * @param   scope
     *          {@code String array} the array of permissions that you want to request.
     *          Ex: RECEIVE_FUNDS, MANAGE_ACCOUNT_INFO, TRANSFER_FUNDS...
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code String}
     */
    public String buildUrl(String clientId, String redirectUri, String[] scope, Setup setup) {

        String url = setup.getEnvironment() + "/oauth/authorize";

        url += String.format("?response_type=code&client_id=%s&redirect_uri=%s&scope=", clientId, redirectUri);

        for (Integer index = 0; index < scope.length; ++index) {

            url += scope[index];

            if ((index + 1) < scope.length) url += ',';
        }
        return url;
    }

    /**
     *  This method allows you to generate access token for a Moip account. With this access token, you can
     *  make some request involving more than one Moip account. Before generate it, you must request
     *  access permission for your user to get the code returned on response to generate the token.
     *
     * @param   body
     *          {@code Map<String, Object>} the request body.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> generateAccessToken(Map<String, Object> body, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(ENDPOINT)
                .body(body)
                .type(Connect.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }

    /**
     * This method allows you to refresh the access token of a Moip account, if you lost the token
     * or it has expired.
     *
     * @param   body
     *          {@code Map<String, Object>} the request body.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return {@code Map<String, Object>}
     */
    public Map<String, Object> refreshAccessToken(Map<String, Object> body, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(ENDPOINT)
                .body(body)
                .type(Connect.class)
                .contentType(CONTENT_TYPE)
                .build();

        return this.requestMaker.doRequest(props);
    }
}
