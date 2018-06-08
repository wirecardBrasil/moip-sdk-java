package br.com.moip.api.request;

import br.com.moip.auth.Authentication;
import br.com.moip.util.GsonFactory;
import com.google.gson.Gson;

public class RequestMaker {

    private final String endpoint;
    private final Authentication authentication;
    private final Gson gson;

    /**
     *
     * @param endpoint
     * @param authentication
     */
    public RequestMaker(final String endpoint, final Authentication authentication) {
        this.endpoint = endpoint;
        this.authentication = authentication;
        this.gson = GsonFactory.gson();
    }
}
