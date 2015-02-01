package com.bahj.smelt.plugin.builtin.data.model.type;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * An interface representing a type in a Smelt data model.
 * @param <V> The Java representation of a value of this type.
 * @author Zachary Palmer
 */
public interface SmeltType<V extends SmeltValue<V>> {
    /**
     * Retrieves a name for this Smelt type.  Smelt type names are unique within a data model.
     * @return The name of this Smelt type.
     */
    public String getName();
    
    /**
     * Instantiates a default value for this type.  Callers should bear in mind that the resulting {@link SmeltValue}
     * may be <code>null</code>.
     * @return A default value for this type.
     */
    public V instantiate();
}
