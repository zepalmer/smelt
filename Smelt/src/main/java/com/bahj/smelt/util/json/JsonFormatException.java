package com.bahj.smelt.util.json;

/**
 * An exception raised when a JSON object is not of an expected format.
 * 
 * @author Zachary Palmer
 */
public class JsonFormatException extends Exception {
    private static final long serialVersionUID = 1L;

    public JsonFormatException() {
        super();
    }

    public JsonFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JsonFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonFormatException(String message) {
        super(message);
    }

    public JsonFormatException(Throwable cause) {
        super(cause);
    }
}
