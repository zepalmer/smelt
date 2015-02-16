package com.bahj.smelt.plugin.builtin.data.model.type;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;

/**
 * An interface representing a type in a Smelt data model.
 * 
 * @param <V>
 *            The Java representation of a value of this type.
 * @param <E>
 *            The root event type for this type's values.
 * @author Zachary Palmer
 */
public interface SmeltType<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> {
    /**
     * Retrieves a name for this Smelt type. Smelt type names are unique within a data model.
     * 
     * @return The name of this Smelt type.
     */
    public String getName();

    /**
     * Instantiates a default value for this type. Callers should bear in mind that the resulting {@link SmeltValue} may
     * be <code>null</code>.
     * 
     * @return A default value for this type.
     */
    public V instantiate();

    /**
     * Coerces the provided value to the data type for this {@link SmeltType}.
     * 
     * @param value
     *            The value to coerce.
     * @return The coerced value, if its runtime type matches the data representation of this {@link SmeltType}.
     * @throws SmeltTypeMismatchException
     *             If the provided value cannot be coerced to this type.
     */
    public V coerce(SmeltValue<?, ?> value) throws SmeltTypeMismatchException;
}
