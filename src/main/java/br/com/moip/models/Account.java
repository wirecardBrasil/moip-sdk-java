package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class Account {

    private static final String ENDPOINT = "/v2/accounts";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    public Map<String, Object> checkExistence()
}
