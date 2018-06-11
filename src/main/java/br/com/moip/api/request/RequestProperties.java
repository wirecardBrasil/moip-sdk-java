package br.com.moip.api.request;

import org.apache.http.entity.ContentType;

class RequestProperties {

    protected String method;
    protected String endpoint;
    protected Object object;
    protected Class type;
    protected ContentType contentType;
    protected String accept;

    public String getMethod() { return method; }

    public String getEndpoint() { return endpoint; }

    public Object getObject() { return object; }

    public <T> Class<T> getType() { return type; }

    public ContentType getContentType() { return contentType; }

    public String getAccept() { return accept; }

    /**
     *
     * @return
     */
    public boolean hasAccept() {

        if (this.accept == null) { return false; }

        return true;
    }

}
