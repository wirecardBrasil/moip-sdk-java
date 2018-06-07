package br.com.moip.exception;

import br.com.moip.models.error.Errors;

public class ValidationException extends MoipAPIException {

    private final int responseCode;
    private final String responseStatus;
    private final Errors errors;

    /**
     * This constructor is used to set the code, status and errors
     * from api response, when it trows a ValidationException.
     *
     * @param   responseCode
     *          {@code int} api response code
     *
     * @param   responseStatus
     *          {@code String} api response status
     *
     * @param   errors
     *          {@code Errors} api errors
     */
    public ValidationException(final int responseCode, final String responseStatus, final Errors errors) {
        this.responseCode = responseCode;
        this.responseStatus = responseStatus;
        this.errors = errors;
    }

    public int getResponseCode() { return responseCode; }

    public String getResponseStatus() { return responseStatus; }

    public Errors getErrors() { return errors; }
}
