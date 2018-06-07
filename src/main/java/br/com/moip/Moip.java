package br.com.moip;

public class Moip {

    private static final String SANDBOX_URL = "https://sandbox.moip.com.br/v2";

    private static final String PRODUCTION_URL = "https://api.moip.com.br/v2";

    private static final String CONNECT_SANDBOX_URL = "https://connect-sandbox.moip.com.br";

    private static final String CONNECT_PRODUCTION_URL = "https://connect.moip.com.br";

    // Default connect timeout (in milliseconds) with default value.
    private static int CONNECT_TIMEOUT = 60 * 1000;

    // Read timeout (in milliseconds) with default value.
    private static int READ_TIMEOUT = 80 * 1000;

    public static int getConnectTimeout() { return CONNECT_TIMEOUT; }

    public static int getReadTimeout() { return READ_TIMEOUT; }

    /**
     * Use this method will change the connect timeout default value.
     * It will be used to api the Moip APIs.
     *
     * @param   connectTimeout
     *          {@code int} timeout in milliseconds
     */
    public static void setConnectTimeout(final int connectTimeout) { CONNECT_TIMEOUT = connectTimeout; }

    /**
     * Use this method will change the read timeout default value.
     * It will be used to api the Moip APIs.
     *
     * @param   readTimeout
     *          {@code int} timeout in millisecond
     */
    public static void setReadTimeout(final int readTimeout) { READ_TIMEOUT = readTimeout; }
}
