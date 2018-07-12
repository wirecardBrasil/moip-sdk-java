package br.com.moip.utilities;

import br.com.moip.api.request.APIDateRequest;
import br.com.moip.models.APIDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class GsonFactory {

    /**
     *
     * @return
     */
    public static Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .registerTypeAdapter(Date.class, new BirthDateRequestSerializer())
                .registerTypeAdapter(APIDateRequest.class, new ApiDateSerializer())
                .registerTypeAdapter(APIDate.class, new ApiDateDeserializer())
                .create();
    }
}
