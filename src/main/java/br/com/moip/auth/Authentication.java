package br.com.moip.auth;

import java.net.HttpURLConnection;

public interface Authentication {

    void authenticate(HttpURLConnection connection);
}
