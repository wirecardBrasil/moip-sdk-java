package br.com.moip.api.request;

import org.apache.http.entity.ContentType;

import java.util.Map;

public class RequestProperties {

    protected String method;
    protected String endpoint;
    protected Map<String, Object> body;
    protected Class type;
    protected ContentType contentType;
    protected String accept;

    public String getMethod() { return method; }

    public String getEndpoint() { return endpoint; }

    public Object getObject() { return body; }

    public <T> Class<T> getType() { return type; }

    public ContentType getContentType() { return contentType; }

    public String getAccept() { return accept; }

    /**
     * This method is used to verify if the {@code Accept} header was settled into properties.
     *
     * @return  {@code boolean}
     */
    public boolean hasAccept() {

        if (this.accept == null) { return false; }

        return true;
    }

}
