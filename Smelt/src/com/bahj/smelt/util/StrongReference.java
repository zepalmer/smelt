package com.bahj.smelt.util;

/**
 * An object simply defining an indirection.
 * @author Zachary Palmer
 *
 * @param <T> The type of indirection.
 */
public class StrongReference<T> {
    private T value;

    public StrongReference(T value) {
        super();
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }}
