package br.com.moip.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class Parser {

    /**
     * This method is used to cast a {@code Object} to {@code Map<String, Object>}. The main
     * objective of this method is objectToMap the objects from response Map to make possible
     * catch its attributes' values.
     *
     * @param   object
     *          {@code Object} any Object type.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> objectToMap(Object object) {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.convertValue(object, Map.class);
    }

    /**
     *
     * @param object
     * @return
     */
    public List<Map<String, Object>> objectToList(Object object) {
        return (List<Map<String, Object>>)object;
    }
}
