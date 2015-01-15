package com.bahj.smelt.util.partialorder;

import java.util.Set;

/**
 * Thrown when a partial order is inconsistent (in that it contains a cycle).
 */
public class InconsistentPartialOrderException extends Exception {
    private static final long serialVersionUID = 1L;

    private Set<?> cycle;

    public InconsistentPartialOrderException(Set<?> cycle) {
        super();
        initialize(cycle);
    }

    public InconsistentPartialOrderException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace, Set<?> cycle) {
        super(message, cause, enableSuppression, writableStackTrace);
        initialize(cycle);
    }

    public InconsistentPartialOrderException(String message, Throwable cause, Set<?> cycle) {
        super(message, cause);
        initialize(cycle);
    }

    public InconsistentPartialOrderException(String message, Set<?> cycle) {
        super(message);
        initialize(cycle);
    }

    public InconsistentPartialOrderException(Throwable cause, Set<?> cycle) {
        super(cause);
        initialize(cycle);
    }

    private void initialize(Set<?> cycle) {
        this.cycle = cycle;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Set<?> getCycle() {
        return cycle;
    }
}
