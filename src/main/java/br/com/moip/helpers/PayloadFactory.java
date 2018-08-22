package br.com.moip.helpers;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PayloadFactory {

    /**
     * This method was created to simplify the construction of request payloads. However, the use of this method
     * shouldn't be compulsory and you can also use plain old Maps.
     *
     * @param   entries
     *          {@code Map.Entry<String, Object>} the entries that will describe the request payload.
     *
     * @return  {@code Map<String, Object>}
     */
    @SafeVarargs
    public static Map<String, Object> payloadFactory(Map.Entry<String, Object>... entries) {
        Map<String, Object> map = new HashMap<>();

        for (Map.Entry<String, Object> entry : entries)
            map.put(entry.getKey(), entry.getValue());

        return Collections.unmodifiableMap(map);
    }

    /**
     * This is a auxiliary method used to load the {@code Map} returned from {@code payloadFactory}. The usage
     * is similar to {@code Map} interaction, but its simplify the dynamic usage of {@code payloadFactory}.
     *
     * @param   key
     *          {@code String} the attribute key.
     *
     * @param   value
     *          {@code Object} the attribute value.
     *
     * @return  {@code Map.Entry<String, Object>}
     */
    public static Map.Entry<String, Object> value(String key, Object value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }
}
