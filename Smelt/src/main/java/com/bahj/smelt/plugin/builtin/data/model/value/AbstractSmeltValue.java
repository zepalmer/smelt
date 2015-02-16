package com.bahj.smelt.plugin.builtin.data.model.value;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;
import com.bahj.smelt.util.event.AbstractEventGenerator;

public abstract class AbstractSmeltValue<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> extends
        AbstractEventGenerator<E> implements SmeltValue<V, E> {
    private SmeltType<V, E> type;

    public AbstractSmeltValue(SmeltType<V, E> type) {
        super();
        this.type = type;
    }

    @Override
    public SmeltType<V, E> getType() {
        return type;
    }
}
