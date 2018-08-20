package br.com.moip.api.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response extends HashMap<String, Object> {

    private Map<String, Object> responseBody = new HashMap<>();
    private List<Map<String, Object>> responseBodyList = new ArrayList<>();

    /**
     * This method is used to receive the JSON returned from API and cast it to a Map.
     *
     * @param   json
     *          {@code String} the JSON returned from API, by {@code InputStream}.
     *
     * @return  {@code Map}
     */
    public Map<String, Object> jsonToMap(String json) {

        /*
         * This if block treats the /v2/accounts/exists response. Currently the endpoint returns the status
         * code on its response body, breaking the JSON conversion.
         */
        if ("200".equals(json)) {
            this.responseBody.put("code", 200);
            return this.responseBody;
        }

        if ("400".equals(json)) {
            this.responseBody.put("code", 400);
            return this.responseBody;
        }

        if ("404".equals(json)) {
            this.responseBody.put("code", 404);
            return this.responseBody;
        }

        if (!json.equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.responseBody = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.responseBody;
    }

    /**
     *
     * @param json
     * @return
     */
    public List<Map<String, Object>> jsonToList(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.responseBodyList = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.responseBodyList;
    }
}
