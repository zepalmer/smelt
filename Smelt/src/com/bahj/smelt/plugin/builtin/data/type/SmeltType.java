package com.bahj.smelt.plugin.builtin.data.type;

import com.bahj.smelt.plugin.builtin.data.value.SmeltValue;

/**
 * An interface representing a type in a Smelt data model.
 * @param <V> The Java representation of a value of this type.
 * @author Zachary Palmer
 */
public interface SmeltType<V extends SmeltValue> {
    /**
     * Retrieves a name for this Smelt type.  Smelt type names are unique within a data model.
     * @return The name of this Smelt type.
     */
    public String getName();
}
