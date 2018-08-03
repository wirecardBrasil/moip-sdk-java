package br.com.moip.integration_tests;

import br.com.moip.Moip;
import br.com.moip.auth.Authentication;
import br.com.moip.auth.OAuth;
import br.com.moip.models.Setup;

import java.util.HashMap;
import java.util.Map;

public class ConnectTest {

    private static Authentication auth = new OAuth("8833c9eb036543b6b0acd685a76c9ead_v2");

    private Setup setup = new Setup().setAuthentication(auth).setEnvironment(Setup.Environment.CONNECT_SANDBOX);

    public void buildUrlTest() {

        String[] scope = {"TRANSFER_FUNDS", "RECEIVE_FUNDS"};

        String url = Moip.API.connect().buildUrl("APP-DVLJHW59IKOS", "http://www.exemplo.com.br/retorno", scope);

        System.out.println(url);
    }

    public void generateAccessTokenTest() {

        Map<String, Object> body = new HashMap<>();
        body.put("client_id", "APP-DVLJHW59IKOS");
        body.put("client_secret", "31afe1ce89fe45b7975ade3024007370");
        body.put("redirect_uri", "http://www.exemplo.com.br/retorno");
        body.put("grant_type", "authorization_code");
        body.put("code", "b50f8d4c3932a38903d842bed70783db4183d04e");

        Map<String, Object> accessToken = Moip.API.connect().generateAccessToken(body, setup);

        System.out.println(accessToken);
    }

    public void refreshAccessTokenTest() {

        Map<String, Object> body = new HashMap<>();
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", "599151f5954e401c8d169d5fa83c25ba_v2");

        Map<String, Object> refreshToken = Moip.API.connect().refreshAccessToken(body, setup);

        System.out.println(refreshToken);
    }
}
