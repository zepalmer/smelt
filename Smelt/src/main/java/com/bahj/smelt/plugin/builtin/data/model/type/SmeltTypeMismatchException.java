package com.bahj.smelt.plugin.builtin.data.model.type;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * Raised when a {@link SmeltValue} is expected to be of a particular {@link SmeltType} but is not.
 * 
 * @author Zachary Palmer
 */
public class SmeltTypeMismatchException extends Exception {
    private static final long serialVersionUID = 1L;

    private SmeltType<?, ?> type;
    private SmeltValue<?, ?> value;

    public SmeltTypeMismatchException(SmeltType<?, ?> type, SmeltValue<?, ?> value) {
        super();
        this.type = type;
        this.value = value;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public SmeltType<?, ?> getType() {
        return type;
    }

    public SmeltValue<?, ?> getValue() {
        return value;
    }
}
