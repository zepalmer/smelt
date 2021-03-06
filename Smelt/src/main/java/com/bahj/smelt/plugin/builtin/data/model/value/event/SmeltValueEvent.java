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
public interface SmeltValueEvent<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> extends Event {
    /**
     * Retrieves the Smelt value which is affected by this event.
     * 
     * @return The value.
     */
    public V getValue();
}
