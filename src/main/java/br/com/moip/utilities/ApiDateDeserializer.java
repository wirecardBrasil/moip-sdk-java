package br.com.moip.utilities;

import br.com.moip.models.APIDate;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ApiDateDeserializer implements JsonDeserializer<APIDate> {

    /**
     *
     * @param json
     * @param typeOfT
     * @param context
     * @return
     * @throws JsonParseException
     */
    @Override
    public APIDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        APIDate apiDate = new APIDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            apiDate.setDate(simpleDateFormat.parse(json.getAsJsonPrimitive().getAsString()));
            return apiDate;
        } catch (ParseException e) {
            return null;
        }
    }
}
