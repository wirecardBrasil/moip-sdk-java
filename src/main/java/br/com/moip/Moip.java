package br.com.moip;

import br.com.moip.api.APIResources;
import br.com.moip.api.request.RequestMaker;

import java.io.InputStream;
import java.util.Properties;

public class Moip {

    public static final String SANDBOX_URL = "https://sandbox.moip.com.br";

    public static final String PRODUCTION_URL = "https://api.moip.com.br";

    public static final String CONNECT_SANDBOX_URL = "https://connect-sandbox.moip.com.br";

    public static final String CONNECT_PRODUCTION_URL = "https://connect.moip.com.br";

    private static String USER_AGENT;

    static {
        try {
            InputStream inputStream = RequestMaker.class.getResourceAsStream("/moipJavaSDK.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            USER_AGENT = properties.getProperty("userAgent");
        } catch (Exception e) {
            USER_AGENT = "MoipJavaSDK/UnknownVersion (+https://github.com/moip/moip-sdk-java/)";
        }
    }

    /**
     * This method returns the {@code USER_AGENT} value.
     *
     * @return  {@code String}
     */
    protected String getUserAgent() { return USER_AGENT; }

    public static class API extends APIResources {}
}
