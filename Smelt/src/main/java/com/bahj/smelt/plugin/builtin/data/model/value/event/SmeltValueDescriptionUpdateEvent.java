package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * An event type which is raised when the description of a {@link SmeltValue} changes.
 * 
 * @author Zachary Palmer
 */
public interface SmeltValueDescriptionUpdateEvent<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> {

}
