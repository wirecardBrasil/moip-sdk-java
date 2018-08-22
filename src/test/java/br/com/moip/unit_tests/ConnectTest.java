package br.com.moip.unit_tests;

import br.com.moip.Moip;
import br.com.moip.models.Setup;
import br.com.moip.unit_tests.setup.SetupFactory;
import br.com.moip.utilities.Parser;
import com.rodrigosaito.mockwebserver.player.Play;
import com.rodrigosaito.mockwebserver.player.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ConnectTest {

    @Rule
    public Player player = new Player();
    private Setup setup;
    private Map<String, Object> body;
    private Parser parser;

    @Before
    public void initialize() {
        this.body = new HashMap<>();
        this.setup = new SetupFactory().setupOAuth(player.getURL("").toString());
        this.parser = new Parser();
    }

    @Test
    public void buildUrl() {

        this.setup = new Setup().setEnvironment(Setup.Environment.CONNECT_SANDBOX);

        String[] scope = {"TRANSFER_FUNDS", "RECEIVE_FUNDS"};

        String url = Moip.API.connect().buildUrl("APP-DVLJHW59IKOS", "http://www.exemplo.com.br/retorno", scope, setup);

        assertEquals("https://connect-sandbox.moip.com.br/oauth/authorize?response_type=code&client_id=APP-DVLJHW59IKOS" +
                "&redirect_uri=http://www.exemplo.com.br/retorno&scope=TRANSFER_FUNDS,RECEIVE_FUNDS", url);
    }

    @Play("connect/generate_access_token")
    @Test
    public void generateAccessTokenTest() {

        Map<String, Object> accessToken = Moip.API.connect().generateAccessToken(body, setup);

        assertEquals("0c6937d062874507b9c19748d8e0f8df_v2", accessToken.get("accessToken"));
        assertEquals("0c6937d062874507b9c19748d8e0f8df_v2", accessToken.get("access_token"));
        assertEquals("2028-08-02", accessToken.get("expires_in"));
        assertEquals("c1384b7aa4a8432b8fcfad26622cb917_v2", accessToken.get("refreshToken"));
        assertEquals("c1384b7aa4a8432b8fcfad26622cb917_v2", accessToken.get("refresh_token"));
        assertEquals("TRANSFER_FUNDS,RECEIVE_FUNDS", accessToken.get("scope"));

        Map<String, Object> moipAccount = parser.objectToMap(accessToken.get("moipAccount"));
        assertEquals("MPA-14C9EE706C55", moipAccount.get("id"));
    }

    @Play("connect/refresh_access_token")
    @Test
    public void refreshAccessTokenTest() {

        Map<String, Object> accessToken = Moip.API.connect().generateAccessToken(body, setup);

        assertEquals("fb65d9d88bdf47d39d90ead6b290e17b_v2", accessToken.get("accessToken"));
        assertEquals("fb65d9d88bdf47d39d90ead6b290e17b_v2", accessToken.get("access_token"));
        assertEquals("2028-08-02", accessToken.get("expires_in"));
        assertEquals("e466a50bb6fb414aa0ea937ddf6ca71b_v2", accessToken.get("refreshToken"));
        assertEquals("e466a50bb6fb414aa0ea937ddf6ca71b_v2", accessToken.get("refresh_token"));
        assertEquals("TRANSFER_FUNDS,RECEIVE_FUNDS", accessToken.get("scope"));

        Map<String, Object> moipAccount = parser.objectToMap(accessToken.get("moipAccount"));
        assertEquals("MPA-14C9EE706C55", moipAccount.get("id"));
    }
}
