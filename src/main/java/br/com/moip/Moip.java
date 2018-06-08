package br.com.moip;

import br.com.moip.models.Setup;

public class Moip {

    private Setup setup;

    private static final String SANDBOX_URL = "https://sandbox.moip.com.br/v2";

    private static final String PRODUCTION_URL = "https://api.moip.com.br/v2";

    private static final String CONNECT_SANDBOX_URL = "https://connect-sandbox.moip.com.br";

    private static final String CONNECT_PRODUCTION_URL = "https://connect.moip.com.br";

    /**
     * This constructor receives a Setup object to use its attributes
     * to configure and authenticate the Moip connection.
     *
     * @param   setup
     *          {@code Setup} object to configure the connection.
     */
    public Moip(Setup setup) { this.setup = setup; }
}
