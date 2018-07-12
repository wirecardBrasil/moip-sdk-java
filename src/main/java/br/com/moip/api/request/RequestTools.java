package br.com.moip.api.request;

import br.com.moip.utilities.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.entity.ContentType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static br.com.moip.utilities.DataHelper.jsonToUrlEncodedString;

public class RequestTools {

    private final Gson gson = GsonFactory.gson();

    /**
     * Method to get a Gson instance.
     *
     * @return  {@code Gson}
     */
    Gson getGsonInstance() { return gson; }

    /**
     * This method is used to read a {@code InputStream} and build a body object with this stream.
     *
     * @param   inputStream
     *          {@code InputStream} the stream that will charge the body object.
     *
     * @return  {@code StringBuilder}
     *
     * @throws  IOException
     *          This method throws a {@code IOException} if the inputted stream isn't correctly.
     */
    StringBuilder readBody(final InputStream inputStream) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            body.append(inputLine);
        }
        in.close();

        return body;
    }

    /**
     * This method is used to get a serialized object into its equivalent JSON representation.
     *
     * @param   object
     *          {@code Object} the body object.
     *
     * @param   contentType
     *          {@ContentType} the content type header.
     *
     * @return  {@code String}
     */
    String getBody(Object object, ContentType contentType) {
        if (contentType == ContentType.APPLICATION_FORM_URLENCODED) {
            return jsonToUrlEncodedString((JsonObject) new JsonParser().parse(gson.toJson(object)));
        }

        return gson.toJson(object);
    }
}
