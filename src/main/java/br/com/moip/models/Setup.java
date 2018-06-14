package br.com.moip.models;

import br.com.moip.auth.Authentication;

public class Setup {

    private static Authentication authentication;

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
     *          {@code Authentication} to connect with Moip APIs.
     *
     * @return  {@code this} (Setup)
     */
    public Setup setAuthentication(final Authentication authentication) {
        this.authentication = authentication;

        return this;
    }

    /**
     * Use this method will change the connect timeout default value.
     * It will be used to request the Moip APIs.
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
     * It will be used to request the Moip APIs.
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
    public static Authentication getAuthentication() { return authentication; }

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
}
