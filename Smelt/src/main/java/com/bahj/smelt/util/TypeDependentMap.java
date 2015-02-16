package com.bahj.smelt.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A dependently-typed dictionary from data type to objects of that type.
 * 
 * @author Zachary Palmer
 *
 * @param <T>
 *            An upper bound of all objects stored in this map.
 */
public class TypeDependentMap<T> {
    private Map<Class<? extends T>, T> map;

    public TypeDependentMap() {
        this.map = new HashMap<>();
    }

    public <U extends T> U get(Class<U> clazz) {
        @SuppressWarnings("unchecked")
        U value = (U) this.map.get(clazz);
        return value;
    }

    public <U extends T> void put(Class<U> clazz, U value) {
        this.map.put(clazz, value);
    }

    public Map<Class<? extends T>, T> asMap() {
        return Collections.unmodifiableMap(this.map);
    }
}
