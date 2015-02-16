package com.bahj.smelt.serialization;

/**
 * An exception thrown when deserialization of an object fails.
 * 
 * @author Zachary Palmer
 */
public class DeserializationException extends Exception {
    private static final long serialVersionUID = 1L;

    public DeserializationException() {
        super();
    }

    public DeserializationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeserializationException(String message) {
        super(message);
    }

    public DeserializationException(Throwable cause) {
        super(cause);
    }
}
