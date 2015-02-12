package com.bahj.smelt.plugin.builtin.data.model.value.serialization;


/**
 * Implemented by an object which holds metadata about the value serialization process.
 * 
 * @author Zachary Palmer
 */
public interface ValueSerializationContext {
    /**
     * Creates a UID for a given class during this serialization context. If the provided class has not been mapped to
     * an ID yet, a new ID will be selected; otherwise, an existing ID will be returned. In either case, the
     * deserialization process promises that the same ID-class bijection will be present.
     * 
     * @param className The name of the class.
     * @return The ID to use for that class.
     */
    public int getClassNameID(String className);
}
