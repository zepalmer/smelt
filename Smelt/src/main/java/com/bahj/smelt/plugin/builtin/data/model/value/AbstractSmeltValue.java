package com.bahj.smelt.plugin.builtin.data.model.value;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;

public abstract class AbstractSmeltValue<V extends SmeltValue<V>> implements SmeltValue<V> {
    private SmeltType<V> type;

    public AbstractSmeltValue(SmeltType<V> type) {
        super();
        this.type = type;
    }

    @Override
    public SmeltType<V> getType() {
        return type;
    }
}
