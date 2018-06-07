package br.com.moip.models.error;

public class ErrorBuilder extends Error {

    /**
     * This constructor sets the Moip error identifier code.
     * Ex: 'CUS-001', 'ORD-014', 'PAY-103'
     *
     * @param   code
     *          {@code String} Moip error code
     *
     * @return  {@code this} (ErrorBuilder)
     */
    public ErrorBuilder code(final String code) {
        this.setCode(code);

        return this;
    }

    /**
     * This constructor sets the Moip error path.
     * Ex: 'customer.fullname', 'order.receiver', 'fundingInstrument.invalid'
     *
     * @param   path
     *          {@code String} Moip error path
     *
     * @return {@code this} (ErrorBuilder)
     */
    public ErrorBuilder path(final String path) {
        this.setPath(path);

        return this;
    }

    /**
     * This constructor sets the Moip error description.
     * Ex: 'Não foi possível descriptografar o hash do cartão.',
     *     'A estrutura de informações do documento não foi informada.'
     *
     * @param   description
     *          {@code String} Moip error description
     *
     * @return {@code this} (ErrorBuilder)
     */
    public ErrorBuilder description(final String description) {
        this.setDescription(description);

        return this;
    }

    /**
     * This method is used to parse the object to Error type and
     * complete the build.
     *
     * @return {@code this} (Error)
     */
    public Error build() {
        return this;
    }
}
