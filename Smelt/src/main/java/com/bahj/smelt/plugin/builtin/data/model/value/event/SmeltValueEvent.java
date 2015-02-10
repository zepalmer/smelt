package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.util.event.Event;

/**
 * The supertype of all Smelt value events.
 * 
 * @author Zachary Palmer
 * @param <V>
 *            The type of value which is being affected.
 */
public abstract class SmeltValueEvent<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> implements Event {
    private V value;

    public SmeltValueEvent(V value) {
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
