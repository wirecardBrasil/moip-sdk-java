package br.com.moip.models;

import br.com.moip.auth.Authentication;

import static br.com.moip.Moip.PRODUCTION_URL;
import static br.com.moip.Moip.SANDBOX_URL;
import static br.com.moip.Moip.CONNECT_PRODUCTION_URL;
import static br.com.moip.Moip.CONNECT_SANDBOX_URL;

public class Setup {

    private Authentication authentication;

    private String environment;

    // Default connect timeout (in milliseconds) with default value.
    private static int CONNECT_TIMEOUT = 60 * 1000;

    // Read timeout (in milliseconds) with default value.
    private static int READ_TIMEOUT = 80 * 1000;

    /**
     * This method is used to set the authentication that will be
     * used to authorize the request. The authentication may be an
     * BasicAuth or an OAuth.
     *
     * @see br.com.moip.auth.BasicAuth
     * @see br.com.moip.auth.OAuth
     *
     * @param   authentication
     *          {@code Authentication} to connect with Moip APIResources.
     *
     * @return  {@code this} (Setup)
     */
    public Setup setAuthentication(final Authentication authentication) {
        this.authentication = authentication;

        return this;
    }

    /**
     * This method is used to set the Moip API environment where the requests will be sent.
     * The only Moip environments that are possible request are {@code SANDBOX} or {@code PRODUCTION}.
     *
     * @param   environment
     *          {@code String} the Moip API environment.
     *
     * @return  {@code Setup}
     */
    public Setup setEnvironment(final Environment environment) {

        switch (environment) {

            case SANDBOX : this.environment = SANDBOX_URL; break;
            case PRODUCTION : this.environment = PRODUCTION_URL; break;
            case CONNECT_SANDBOX : this.environment = CONNECT_SANDBOX_URL; break;
            case CONNECT_PRODUCTION : this.environment = CONNECT_PRODUCTION_URL; break;

            default : this.environment = "";
        }

        return this;
    }

    /**
     * This method is used to set the {@code Player} endpoint for <strong>mock unit tests</strong>.
     *
     * @param   endpoint
     *          {@code String} the endpoint for mock tests.
     *
     * @return  {@code this} (Setup)
     */
    public Setup setPlayerEndpoint(final String endpoint) {
        this.environment = endpoint;

        return this;
    }

    /**
     * Use this method will change the connect timeout default value.
     * It will be used to request the Moip APIResources.
     *
     * @param   connectTimeout
     *          {@code int} timeout in milliseconds.
     *
     * @return  {@code this} (Setup)
     */
    public Setup setConnectTimeout(final int connectTimeout) {
        CONNECT_TIMEOUT = connectTimeout;

        return this;
    }

    /**
     * Use this method will change the read timeout default value.
     * It will be used to request the Moip APIResources.
     *
     * @param   readTimeout
     *          {@code int} timeout in millisecond.
     *
     * @return  {@code this} (Setup)
     */
    public Setup setReadTimeout(final int readTimeout) {
        READ_TIMEOUT = readTimeout;

        return this;
    }

    /**
     * This method is used to get the value of {@code authentication} attribute.
     *
     * @return  {@code Authentication}
     */
    public Authentication getAuthentication() { return this.authentication; }

    /**
     * This method is used to get the value of {@code environment} attribute.
     *
     * @return  {@code String}
     */
    public String getEnvironment() { return this.environment; }

    /**
     * This method is used to get the value of {@code CONNECT_TIMEOUT} attribute.
     *
     * @return  {@code int}
     */
    public static int getConnectTimeout() { return CONNECT_TIMEOUT; }

    /**
     * This method is used to get the value of {@code READ_TIMEOUT} attribute.
     *
     * @return  {@code int}
     */
    public static int getReadTimeout() { return READ_TIMEOUT; }


    /**
     * These enums are used to difference all Moip API environments.
     */
    public enum Environment { SANDBOX, CONNECT_SANDBOX, PRODUCTION, CONNECT_PRODUCTION }
}
