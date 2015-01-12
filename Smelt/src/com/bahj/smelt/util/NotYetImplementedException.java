package com.bahj.smelt.util;

public class NotYetImplementedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotYetImplementedException() {
        super();
    }

    public NotYetImplementedException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotYetImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotYetImplementedException(String message) {
        super(message);
    }

    public NotYetImplementedException(Throwable cause) {
        super(cause);
    }
}
