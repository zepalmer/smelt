package com.bahj.smelt.model;

/**
 * Raised when a Smelt model cannot be created, often due to a misconfiguration.
 * @author Zachary Palmer
 */
public class ModelCreationException extends Exception {
    private static final long serialVersionUID = 1L;

    public ModelCreationException() {
        super();
    }

    public ModelCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ModelCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelCreationException(String message) {
        super(message);
    }

    public ModelCreationException(Throwable cause) {
        super(cause);
    }
}
