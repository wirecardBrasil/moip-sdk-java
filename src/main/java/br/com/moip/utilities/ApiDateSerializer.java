package br.com.moip.utilities;

import br.com.moip.api.request.APIDateRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

class ApiDateSerializer implements JsonSerializer<APIDateRequest> {

    /**
     *
     * @param src
     * @param typeOfSrc
     * @param context
     * @return
     */
    @Override
    public JsonElement serialize(APIDateRequest src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return new JsonPrimitive(format.format(src.getDate()));
    }
}
