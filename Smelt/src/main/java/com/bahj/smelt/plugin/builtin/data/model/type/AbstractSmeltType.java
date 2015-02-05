package com.bahj.smelt.plugin.builtin.data.model.type;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

public abstract class AbstractSmeltType<V extends SmeltValue<V>> implements SmeltType<V> {
    @Override
    public String toString() {
        return this.getName();
    }
}
