/**
 * Read more <a href="https://dev.moip.com.br/v2.0/reference#erros-1>API Reference</a>
 */
package br.com.moip.models.error;

public class Error {

    private String code;
    private String path;
    private String description;

    public String getCode() { return code; }

    public String getPath() { return path; }

    public String getDescription() { return description; }

    /**
     * This method receive the Moip error identifier code from ErrorBuilder class.
     *
     * @param   code
     *          {@code String} Moip error code
     */
    void setCode(String code) { this.code = code; }

    /**
     * This method receive the Moip error path from ErrorBuilder class.
     *
     * @param   path
     *          {@code String} Moip error path
     */
    void setPath(String path) { this.path = path; }

    /**
     * This method receive the Moip error description from ErrorBuilder class.
     *
     * @param   description
     *          {@code String} Moip error description
     */
    void setDescription(String description) { this.description = description; }
}
