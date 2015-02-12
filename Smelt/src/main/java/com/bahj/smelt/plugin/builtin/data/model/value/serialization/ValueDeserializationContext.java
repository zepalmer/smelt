package com.bahj.smelt.plugin.builtin.data.model.value.serialization;

/**
 * Implemented by an object which holds metadata about the deserialization process.
 * @author Zachary Palmer
 */
public interface ValueDeserializationContext {
    /**
     * Retrieves a class by its ID.
     * @param id The ID to use.
     * @return The class name for that ID or <code>null</code> if no such ID was mapped.
     */
    public String getClassNameByID(int id);
}
