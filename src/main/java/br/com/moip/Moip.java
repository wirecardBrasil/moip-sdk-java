package br.com.moip;

import br.com.moip.api.APIResources;
import br.com.moip.api.request.RequestMaker;
import br.com.moip.models.Setup;

import java.io.InputStream;
import java.util.Properties;

public class Moip {

    private Setup setup;

    public static final String SANDBOX_URL = "https://sandbox.moip.com.br";

    public static final String PRODUCTION_URL = "https://api.moip.com.br";

    public static final String CONNECT_SANDBOX_URL = "https://connect-sandbox.moip.com.br";

    public static final String CONNECT_PRODUCTION_URL = "https://connect.moip.com.br";

    /**
     *
     */
    private static String USER_AGENT;

    static {
        try {
            InputStream inputStream = RequestMaker.class.getResourceAsStream("/moipJavaSDK.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            USER_AGENT = properties.getProperty("userAgent");
        } catch (Exception e) { // verificar tipo de exception
            USER_AGENT = "MoipJavaSDK/UnknownVersion (+https://github.com/moip/moip-sdk-java/)";
        }
    }

    /**
     * This method returns the {@code USER_AGENT} value.
     *
     * @return  {@code String}
     */
    public String getUserAgent() { return this.USER_AGENT; }

    // Default Moip constructor.
    public Moip() {}

    /**
     * This constructor receives a Setup object to use its attributes
     * to configure and authenticate the Moip connection.
     *
     * @param   setup
     *          {@code Setup} object to configure the connection.
     */
    public Moip(Setup setup) { this.setup = setup; }

    /**
     * This method gets the setup instance.
     *
     * @return  {@code Setup}
     */
    public Setup getSetup() { return this.setup; }

    private static class API extends APIResources {}
}
