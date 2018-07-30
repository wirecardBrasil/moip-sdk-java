package br.com.moip.api.request;

import org.apache.http.entity.ContentType;

import java.util.Map;

public class RequestPropertiesBuilder extends RequestProperties {

    // Default constructor.
    public RequestPropertiesBuilder() {}

    /**
     * This method is used to set the request method into super class.
     * The possible values are POST, GET, PUT and DELETE.
     *
     * @param   method
     *          {@code String} request method
     *
     * @return {@code this} (RequestPropertiesBuilder)
     */
    public RequestPropertiesBuilder method(String method) {
        super.method = method;

        return this;
    }

    /**
     * This method is used to set the request endpoint into super class.
     * This is the endpoint that will be requested.
     * Ex: {@code /v2/customers}, {@code /v2/accounts}, {@code /v2/transfers}.
     *
     * @param   endpoint
     *          {@code String} request endpoint.
     *
     * @return  {@code this} (RequestPropertiesBuilder)
     */
    public RequestPropertiesBuilder endpoint(String endpoint) {
        super.endpoint = endpoint;

        return this;
    }

    /**
     * This method is used to set the request object into super class.
     * The object is the body of the request.
     *
     * @param   body
     *          {@code Object} request object
     *
     * @see java.lang.Object
     *
     * @return  {@code this} (RequestPropertiesBuilder)
     */
    public RequestPropertiesBuilder body(Map<String, Object> body) {
        super.body = body;

        return this;
    }

    /**
     * This method is used to set the model class type. If the request
     * is successful, the class attributes will be filled with the response.
     *
     * @param   type
     *          {@code Class} request class type
     *
     * @see java.lang.Class
     *
     * @return  {@code this} (RequestPropertiesBuilder)
     */
    public RequestPropertiesBuilder type(Class type) {
        super.type = type;

        return this;
    }

    /**
     * This method is used to set the request content type.
     * Actually, the only content type accept by Moip API is the {@code APPLICATION_JSON}.
     *
     * @param   contentType
     *          {@code ContentType} request content type
     *
     * @see org.apache.http.entity.ContentType
     *
     * @return  {@code this} (RequestPropertiesBuilder)
     */
    public RequestPropertiesBuilder contentType(ContentType contentType) {
        super.contentType = contentType;

        return this;
    }

    /**
     * This method is used to set the JSON version, if it takes.
     * Actually, only some endpoints accept a different JSON version.
     * Accept {@code application/json;version=2.1}
     * Possible value: {@code "2.1"}
     *
     * @param   acceptVersion
     *          {@code String} accept JSON version
     *
     * @see <a href="https://dev.moip.com.br/v2.0/reference#saldo-moip-1">Saldo Moip</a>
     * @see <a href="https://dev.moip.com.br/v2.0/reference#lan%C3%A7amentos-1">Lançamentos</a>
     *
     * @return  {@code this} (RequestPropertiesBuilder)
     */
    public RequestPropertiesBuilder accept(String acceptVersion) {
        super.accept = acceptBuilder(acceptVersion);

        return this;
    }

    /**
     * This method is used to validate and build the accept JSON version.
     *
     * @param   version
     *          {@code String} version
     *
     * @return  {@code String} the mounted header value
     */
    private String acceptBuilder(String version) {
        String acceptValue = "application/json";
        if(version == "2.1") acceptValue += ";version=" + version;

        return acceptValue;
    }

    /**
     * This method is used to parse the object to RequestProperties type and
     * complete the build.
     *
     * @return {@code this} (RequestProperties)
     */
    public RequestProperties build() { return this; }
}
