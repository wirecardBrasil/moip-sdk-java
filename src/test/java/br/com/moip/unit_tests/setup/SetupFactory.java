package br.com.moip.unit_tests.setup;

import br.com.moip.auth.Authentication;
import br.com.moip.auth.BasicAuth;
import br.com.moip.models.Setup;

public class SetupFactory {

    private static final String token = "01010101010101010101010101010101";
    private static final String key = "ABABABABABABABABABABABABABABABABABABABAB";

    private static final Authentication auth = new BasicAuth(token, key);

    public Setup setup(final String endpoint) {
        return new Setup().setAuthentication(auth).setPlayerEndpoint(endpoint);
    }
}
