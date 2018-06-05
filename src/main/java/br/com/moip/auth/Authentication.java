package br.com.moip.auth;

import java.net.HttpURLConnection;

interface Authentication {

    void authenticate(HttpURLConnection connection);
}
