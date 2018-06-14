package br.com.moip.api.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Response extends HashMap<String, Object> {

    private Map<String, Object> body = new HashMap<>();

    /**
     * This method is used to receive the JSON returned from API and cast it to a Map.
     *
     * @param   json
     *          {@code String} the JSON returned from API, by {@code InputStream}.
     *
     * @return  {@code Map}
     */
    public Map jsonToMap(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            this.body = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.body;
    }
}

/* Este método resolve grande parte do problema, porém não resolve o problema por completo.
 * Precisamos fazer com que cada nó do JSON se converta em um objeto do seu respectivo model.
 * Ex: o nó "taxDocument" precisa ser convertido em um objeto da classe TaxDocument.
 *
 * Talvez seja possível trabalhar com o [type], o atributo da classe RequestProperties, que tem
 * como objetivo indicar a qual classe/model aquele objeto pertence.
 */