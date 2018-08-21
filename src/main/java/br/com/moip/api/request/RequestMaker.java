package br.com.moip.api.request;

import br.com.moip.Moip;
import br.com.moip.api.response.Response;
import br.com.moip.auth.Authentication;
import br.com.moip.exception.MoipAPIException;
import br.com.moip.exception.UnauthorizedException;
import br.com.moip.exception.UnexpectedException;
import br.com.moip.exception.ValidationException;
import br.com.moip.models.Setup;
import br.com.moip.models.error.Errors;
import br.com.moip.utilities.ssl.SSLSupport;

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
    private Response response;


    /**
     * This constructor sets the Moip environment and the authentication received from parameter.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @see br.com.moip.auth.Authentication
     */
    public RequestMaker(Setup setup) {

        this.moipEnvironment = setup.getEnvironment();
        this.authentication = setup.getAuthentication();
        this.tools = new RequestTools();
        this.response = new Response();
    }

    /**
     * This method is used to build the request, it set the environment (Sandbox or Production)
     * and the headers, create the {@code connection} object to establish connection with Moip
     * APIResources, authenticate the connection, load the request properties received from parameter,
     * serialize the request object and send it to the API. Finally, this method receive the
     * response JSON and deserialize it into the respective model object, sending the response
     * code and response body to {@code responseBodyTreatment} method to treat the response.
     *
     * @param   requestProps
     *          {@code RequestProperties} the object containing the properties of
     *          request, its like request method, endpoint, object, type, content type
     *          and if it accepts another JSON version.
     *
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> doRequest(final RequestProperties requestProps) {

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

            if (requestProps.body != null) {
                connection.setDoOutput(true);
                String body = tools.getBody(requestProps.body, requestProps.contentType);

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

            // Return the parsed response from JSON to Map.
            return this.response.jsonToMap(responseBody.toString());

        } catch(IOException | KeyManagementException | NoSuchAlgorithmException e){
            throw new MoipAPIException("Error occurred connecting to Moip API: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @param requestProps
     * @return
     */
    public List<Map<String, Object>> getList(final RequestProperties requestProps) {

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

            if (requestProps.body != null) {
                connection.setDoOutput(true);
                String body = tools.getBody(requestProps.body, requestProps.contentType);

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

            // Return the parsed response from JSON to Map.
            return this.response.jsonToList(responseBody.toString());

        } catch(IOException | KeyManagementException | NoSuchAlgorithmException e){
            throw new MoipAPIException("Error occurred connecting to Moip API: " + e.getMessage(), e);
        }
    }

    /**
     * This method is used to check the response code and apply the correct treatment to the
     * response body. Basically, if the response code is between 200 and 299, this method returns
     * a response body charged with the right input stream. If the response code is 401, this
     * method throws a {@code UnauthorizedException}. If the code is between 400 and 499 (except 401),
     * this method create and charge an {@code Error} object with a deserialize JSON returned from
     * Moip API to build a {@code ValidationException} and throw it. And finally, if the response
     * code is 500 or higher, this method throws a {@code UnexpectedException}.
     *
     * @param   responseBody
     *          {@code StringBuilder} the response body returned from API.
     *
     * @param   responseCode
     *          {@code int} the response code returned from API.
     *
     * @param   connection
     *          {@code HttpURLConnection} the object containing the connection
     *          with Moip API.
     *
     * @return  {@code StringBuilder} response body.
     */
    private StringBuilder responseBodyTreatment(StringBuilder responseBody, int responseCode, HttpURLConnection connection) {

        try {

            if (responseCode >= 200 && responseCode < 299) {
                responseBody = tools.readBody(connection.getInputStream());
            }

            if (responseCode == 401) {
                throw new UnauthorizedException();
            }

            if (responseCode >= 400 && responseCode < 499) {
                responseBody = tools.readBody(connection.getErrorStream());
                LOGGER.debug("API ERROR {}", responseBody.toString());

                Errors errors = new Errors();

                try {

                    errors = tools.getGsonInstance().fromJson(responseBody.toString(), Errors.class);

                } catch (Exception e) {

                    LOGGER.debug("There was not possible cast the JSON to object");
                }

                throw new ValidationException(responseCode, connection.getResponseMessage(), errors);
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
     * This method is used to populate an {@code Map.Entry} with passed keys and values to
     * charge the debug logger.
     *
     * @param   entries
     *          {@code Map.Entry<String, List<String>>}
     */
    private void logHeaders(Set<Map.Entry<String, List<String>>> entries) {
        for (Map.Entry<String, List<String>> header : entries) {
            if (header.getKey() != null) {
                LOGGER.debug("{}: {}", header.getKey(), header.getValue());
            }
        }
    }
}
