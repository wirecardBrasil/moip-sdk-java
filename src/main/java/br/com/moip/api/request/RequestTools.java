package br.com.moip.api.request;

import br.com.moip.util.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.entity.ContentType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static br.com.moip.util.DataHelper.jsonToUrlEncodedString;

public class RequestTools {

    private final Gson gson = GsonFactory.gson();

    /**
     *
     * @return
     */
    Gson getGsonInstance() { return gson; }

    /**
     *
     * @param inputStream
     * @return
     * @throws IOException
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
     *
     * @param object
     * @param contentType
     * @return
     */
    String getBody(Object object, ContentType contentType) {
        if (contentType == ContentType.APPLICATION_FORM_URLENCODED) {
            return jsonToUrlEncodedString((JsonObject) new JsonParser().parse(gson.toJson(object)));
        }

        return gson.toJson(object);
    }
}
