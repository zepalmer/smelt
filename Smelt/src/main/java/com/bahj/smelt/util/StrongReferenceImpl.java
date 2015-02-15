package com.bahj.smelt.util;

public class StrongReferenceImpl<T> implements StrongReference<T> {
    private T value;

    public StrongReferenceImpl(T value) {
        super();
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
