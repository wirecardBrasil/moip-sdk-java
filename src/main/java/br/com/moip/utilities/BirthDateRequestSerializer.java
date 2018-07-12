package br.com.moip.utilities;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class BirthDateRequestSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

    /**
     *
     * @param src
     * @param typeOfSrc
     * @param context
     * @return
     */
    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(sdf().format(src));
    }

    /**
     *
     * @param jsonElement
     * @param type
     * @param jsonDeserializationContext
     * @return
     * @throws JsonParseException
     */
    @Override
    public Date deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        try {
            return sdf().parse(jsonElement.getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    private SimpleDateFormat sdf() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

}
