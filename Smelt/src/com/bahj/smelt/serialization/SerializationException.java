package com.bahj.smelt.serialization;

/**
 * An exception thrown when serialization of an object fails.
 * 
 * @author Zachary Palmer
 */
public class SerializationException extends Exception {
    private static final long serialVersionUID = 1L;

    public SerializationException() {
        super();
    }

    public SerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
