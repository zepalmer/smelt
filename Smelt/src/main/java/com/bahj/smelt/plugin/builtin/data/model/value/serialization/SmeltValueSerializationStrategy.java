package com.bahj.smelt.plugin.builtin.data.model.value.serialization;

import com.bahj.smelt.plugin.builtin.data.model.value.utils.SmeltValueWrapper;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.google.gson.JsonElement;

/**
 * An interface implemented by the serialization strategies used for smelt values.
 * @author Zachary Palmer
 */
public interface SmeltValueSerializationStrategy {
    /**
     * Serializes the provided Smelt value.
     * @param context The context in which serialization occurs.
     * @param obj The object to serialize.
     * @return The JSON-serialized value.
     * @throws SerializationException If an error occurs during serialization.
     */
    public JsonElement objectToJson(ValueSerializationContext context, SmeltValueWrapper<?, ?> obj)
            throws SerializationException;

    /**
     * Deserializes the provided Smelt value.
     * @param context The context in which deserialization occurs.
     * @param jsonElement The JSON element to deserialize.
     * @return The resulting Smelt value.
     * @throws DeserializationException If the provided JSON value cannot be deserialized.
     */
    public SmeltValueWrapper<?, ?> jsonToObject(ValueDeserializationContext context, JsonElement jsonElement)
            throws DeserializationException;
}
