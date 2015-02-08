package com.bahj.smelt.serialization.json;

import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.bahj.smelt.serialization.SmeltStringSerializationStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public interface SmeltJSONSerializationStrategy<T> extends SmeltStringSerializationStrategy<T> {
    /**
     * Transforms an object into a JSON value.
     * 
     * @param obj
     *            The object to transform.
     * @return The JSON representation of that object.
     * @throws SerializationException
     *             If the object cannot be correctly serialized.
     */
    public JsonElement objectToJson(T obj) throws SerializationException;

    /**
     * Transforms a JSON value into an object.
     * 
     * @param json
     *            The JSON value to transform.
     * @return The resulting object.
     * @throws DeserializationException
     *             If the object cannot be correctly deserialized.
     */
    public T jsonToObject(JsonElement json) throws DeserializationException;

    default public String objectToString(T obj) throws SerializationException {
        JsonElement json = objectToJson(obj);
        Gson gson = new Gson();
        return gson.toJson(json);
    }

    default public T stringToObject(String str) throws DeserializationException {
        JsonParser parser = new JsonParser();
        JsonElement json;
        try {
            json = parser.parse(str);
        } catch (JsonSyntaxException e) {
            throw new DeserializationException("Could not parse JSON: " + e.getMessage(), e);
        }
        return jsonToObject(json);
    }
}
