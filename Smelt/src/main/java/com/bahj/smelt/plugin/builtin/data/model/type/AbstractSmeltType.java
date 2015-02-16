package com.bahj.smelt.plugin.builtin.data.model.type;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;

public abstract class AbstractSmeltType<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> implements
        SmeltType<V, E> {
    @Override
    public String toString() {
        return this.getName();
    }
}
