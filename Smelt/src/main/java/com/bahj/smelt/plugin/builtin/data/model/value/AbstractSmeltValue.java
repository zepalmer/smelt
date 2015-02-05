package com.bahj.smelt.plugin.builtin.data.model.value;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;
import com.bahj.smelt.util.event.AbstractEventGenerator;

public abstract class AbstractSmeltValue<V extends SmeltValue<V>, E extends SmeltValueEvent<V>> extends
        AbstractEventGenerator<E> implements SmeltValue<V> {
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
