package com.bahj.smelt.plugin.builtin.data.model.model;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;

/**
 * Raised when a type of a duplicate name is added to a Smelt data model.
 * 
 * @author Zachary Palmer
 */
public class DuplicateTypeNameException extends Exception {
    private static final long serialVersionUID = 1L;

    private String name;
    private SmeltType<?, ?> type1;
    private SmeltType<?, ?> type2;

    public DuplicateTypeNameException(String name, SmeltType<?, ?> type1, SmeltType<?, ?> type2) {
        super();
        initialize(name, type1, type2);
    }

    public DuplicateTypeNameException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace, String name, SmeltType<?, ?> type1, SmeltType<?, ?> type2) {
        super(message, cause, enableSuppression, writableStackTrace);
        initialize(name, type1, type2);
    }

    public DuplicateTypeNameException(String message, Throwable cause, String name, SmeltType<?, ?> type1,
            SmeltType<?, ?> type2) {
        super(message, cause);
        initialize(name, type1, type2);
    }

    public DuplicateTypeNameException(String message, String name, SmeltType<?, ?> type1, SmeltType<?, ?> type2) {
        super(message);
        initialize(name, type1, type2);
    }

    public DuplicateTypeNameException(Throwable cause, String name, SmeltType<?, ?> type1, SmeltType<?, ?> type2) {
        super(cause);
        initialize(name, type1, type2);
    }

    private void initialize(String name, SmeltType<?, ?> type1, SmeltType<?, ?> type2) {
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
    }

    /**
     * Retrieves the type name which was duplicated.
     * 
     * @return The duplicate type name.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the first duplicate type.
     * 
     * @return The first duplicate type.
     */
    public SmeltType<?, ?> getType1() {
        return type1;
    }

    /**
     * Retrieves the second duplicate type.
     * 
     * @return The second duplicate type.
     */
    public SmeltType<?, ?> getType2() {
        return type2;
    }
}
