package com.bahj.smelt.plugin.builtin.data.model.value;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;

public interface SmeltValue<V extends SmeltValue<V>> {
    /**
     * Retrieves the type of this Smelt value.
     * @return The type of this value.
     */
    public SmeltType<V> getType();
}
