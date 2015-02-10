package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * An interface which should be implemented by any {@link SmeltValueEvent} representing a value update.
 * 
 * @author Zachary Palmer
 *
 * @param <V>
 *            The type of Smelt value being updated.
 * @param <E>
 *            The value's event type.
 * @param <P>
 *            The type of the property being updated (e.g. {@link String} for a {@link SmeltText}).
 */
public interface SmeltValueUpdateEvent<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>, P> {
    public V getValue();

    public P getOldValue();

    public P getNewValue();
}
