package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * The supertype of all Smelt value events.
 * 
 * @author Zachary Palmer
 * @param <V>
 *            The type of value which is being affected.
 */
public abstract class AbstractSmeltValueEvent<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>>
        implements SmeltValueEvent<V, E> {
    private V value;

    public AbstractSmeltValueEvent(V value) {
        super();
        this.value = value;
    }

    /**
     * Retrieves the Smelt value which is affected by this event.
     * 
     * @return The value.
     */
    public V getValue() {
        return value;
    }
}
