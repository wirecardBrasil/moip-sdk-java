package br.com.moip.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ObjectToMap {

    public Map<String, Object> convert(Object object) {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.convertValue(object, Map.class);
    }
}
