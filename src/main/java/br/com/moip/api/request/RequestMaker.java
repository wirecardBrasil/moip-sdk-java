package br.com.moip.api.request;

import br.com.moip.Moip;
import br.com.moip.auth.Authentication;
import br.com.moip.exception.MoipAPIException;
import br.com.moip.exception.UnauthorizedException;
import br.com.moip.exception.UnexpectedException;
import br.com.moip.exception.ValidationException;
import br.com.moip.models.error.Errors;
import br.com.moip.util.ssl.SSLSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestMaker extends Moip {

    private final String moipEnvironment;
    private final Authentication authentication;
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestMaker.class);
    private RequestTools tools;


    /**
     *
     * @param moipEnvironment
     * @param authentication
     */
    public RequestMaker(final String moipEnvironment, final Authentication authentication) {
        this.moipEnvironment = moipEnvironment;
        this.authentication = authentication;
    }

    /**
     *
     * @param requestProps
     * @param <T>
     * @return
     */
    public <T> T doRequest(final RequestProperties requestProps) {

        try {

            URL url = new URL(moipEnvironment + requestProps.endpoint);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", getUserAgent());
            connection.setRequestProperty("Content-type", requestProps.getContentType().getMimeType());

            if (requestProps.hasAccept()) connection.setRequestProperty("Accept", requestProps.accept);

            connection.setRequestMethod(requestProps.method);

            // This validation disable the TLS 1.0
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(new SSLSupport());
            }

            if (this.authentication != null) authentication.authenticate(connection);

            LOGGER.debug("---> {} {}", requestProps.method, connection.getURL().toString());
            logHeaders(connection.getRequestProperties().entrySet());

            if (requestProps.object != null) {
                connection.setDoOutput(true);
                String body = tools.getBody(requestProps.object, requestProps.contentType);

                LOGGER.debug("{}", body);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(body);
                writer.close();
                wr.flush();
                wr.close();
            }

            LOGGER.debug("---> END HTTP");

            int responseCode = connection.getResponseCode();

            LOGGER.debug("<--- {} {}", responseCode, connection.getResponseMessage());
            logHeaders(connection.getHeaderFields().entrySet());

            StringBuilder responseBody = new StringBuilder();

            responseBody = responseBodyTreatment(responseBody, responseCode, connection);

            LOGGER.debug("{}", responseBody.toString());
            LOGGER.debug("<-- END HTTP ({}-byte body)", connection.getContentLength());

            return tools.getGsonInstance().fromJson(responseBody.toString(), requestProps.<T>getType());

        } catch(IOException | KeyManagementException | NoSuchAlgorithmException e){
            throw new MoipAPIException("Error occurred connecting to Moip API: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @param responseBody
     * @param responseCode
     * @param conn
     * @return
     */
    private StringBuilder responseBodyTreatment(StringBuilder responseBody, int responseCode, HttpURLConnection conn) {

        try {

            if (responseCode >= 200 && responseCode < 299) {
                responseBody = tools.readBody(conn.getInputStream());
            }

            if (responseCode == 401) {
                throw new UnauthorizedException();
            }

            if (responseCode >= 400 && responseCode < 499) {
                responseBody = tools.readBody(conn.getErrorStream());
                LOGGER.debug("API ERROR {}", responseBody.toString());

                Errors errors = new Errors();

                try {

                    errors = tools.getGsonInstance().fromJson(responseBody.toString(), Errors.class);

                } catch (Exception e) {

                    LOGGER.debug("There was not possible cast the JSON to object");
                }

                throw new ValidationException(responseCode, conn.getResponseMessage(), errors);
            }

            if (responseCode >= 500) {
                throw new UnexpectedException();
            }

        } catch (IOException e) {
            throw new MoipAPIException("Error occurred connecting to Moip API: " + e.getMessage(), e);
        }

        return responseBody;
    }

    /**
     *
     * @param entries
     */
    private void logHeaders(Set< Map.Entry<String, List<String>>> entries) {
        for (Map.Entry<String, List<String>> header : entries) {
            if (header.getKey() != null) {
                LOGGER.debug("{}: {}", header.getKey(), header.getValue());
            }
        }
    }
}
