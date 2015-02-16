package com.bahj.smelt.plugin.builtin.data.model.value;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;
import com.bahj.smelt.util.event.EventGenerator;

public interface SmeltValue<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> extends EventGenerator<E> {
    /**
     * Retrieves the type of this Smelt value.
     * 
     * @return The type of this value.
     */
    public SmeltType<V, E> getType();

    /**
     * Retrieves a human-readable description of this Smelt value.
     * 
     * @return A string summarizing this Smelt value.
     */
    public String getDescription();
}
