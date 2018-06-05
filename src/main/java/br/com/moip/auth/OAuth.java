package br.com.moip.auth;

import java.net.HttpURLConnection;

public class OAuth implements Authentication {

    private final String accessToken;

    public OAuth(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() { return accessToken; }

    public void authenticate(HttpURLConnection connection) {
        connection.addRequestProperty("Authorization", "OAuth " + accessToken);
    }
}
