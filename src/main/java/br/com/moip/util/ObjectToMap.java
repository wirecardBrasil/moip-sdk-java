package br.com.moip.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 *
 */
public class ObjectToMap {

    /**
     * This method is used to cast a {@code Object} to {@code Map<String, Object>}. The main
     * objective of this method is convert the objects from response Map to make possible
     * catch its attributes' values.
     *
     * @param   object
     *          {@code Object} any Object type.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> convert(Object object) {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.convertValue(object, Map.class);
    }
}
