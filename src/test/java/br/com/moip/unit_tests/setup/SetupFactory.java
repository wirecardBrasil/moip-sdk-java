package br.com.moip.unit_tests.setup;

import br.com.moip.auth.Authentication;
import br.com.moip.auth.BasicAuth;
import br.com.moip.auth.OAuth;
import br.com.moip.models.Setup;

public class SetupFactory {

    private static final String token = "01010101010101010101010101010101";
    private static final String key = "ABABABABABABABABABABABABABABABABABABABAB";

    private static final String accessToken = "8833c9eb036543b6b0acd685a76c9ead_v2";

    private static final Authentication basic = new BasicAuth(token, key);
    private static final Authentication oauth = new OAuth(accessToken);

    public Setup setupBasicAuth(final String endpoint) {
        return new Setup().setAuthentication(basic).setPlayerEndpoint(endpoint);
    }

    public Setup setupOAuth(final String endpoint) {
        return new Setup().setAuthentication(oauth).setPlayerEndpoint(endpoint);
    }
}
