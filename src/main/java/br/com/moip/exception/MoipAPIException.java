package br.com.moip.exception;

public class MoipAPIException extends RuntimeException {

    /**
     * Default class constructor.
     */
    public MoipAPIException() {}

    /**
     * Constructor to receive the exception message.
     *
     * @param   message
     *          {@code String} exception message
     */
    public MoipAPIException(final String message) {
        super(message);
    }

    /**
     * Constructor to receive the exception cause.
     *
     * @param   cause
     *          {@code Throwable} exception cause.
     */
    public MoipAPIException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructor to receive the exception message and cause.
     *
     * @param   message
     *          {@code String} exception message
     *
     * @param   cause
     *          {@code Throwable} exception cause
     */
    public MoipAPIException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor to receive the message and the cause of an exception
     * and to able the suppression and the writable stack trace.
     *
     * @param   message
     *          {@code String} exception message
     *
     * @param   cause
     *          {@code Throwable} exception cause
     *
     * @param   enableSuppression
     *          {@code boolean} exception suppression
     *
     * @param   writableStackTrace
     *          {@code boolean} exception stack trace
     */
    public MoipAPIException(final String message, final Throwable cause,
                            final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
