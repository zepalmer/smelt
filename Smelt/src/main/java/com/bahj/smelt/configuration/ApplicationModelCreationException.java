package com.bahj.smelt.configuration;

/**
 * Raised when a Smelt model cannot be created, often due to a misconfiguration.
 * @author Zachary Palmer
 */
public class ApplicationModelCreationException extends Exception {
    private static final long serialVersionUID = 1L;

    public ApplicationModelCreationException() {
        super();
    }

    public ApplicationModelCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ApplicationModelCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationModelCreationException(String message) {
        super(message);
    }

    public ApplicationModelCreationException(Throwable cause) {
        super(cause);
    }
}
