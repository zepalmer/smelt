package com.bahj.smelt.util;

/**
 * An object simply defining a mutable indirection.
 * 
 * @author Zachary Palmer
 *
 * @param <T>
 *            The type of indirection.
 */
public interface StrongReference<T> extends ReadableStrongReference<T> {

    public void setValue(T value);

}