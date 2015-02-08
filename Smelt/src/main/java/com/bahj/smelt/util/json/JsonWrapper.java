package com.bahj.smelt.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * A wrapper type for {@link JsonElement} which provides a number of checking and coercion routines, each of which makes
 * use of checked exceptions.
 * @author Zachary Palmer
 */
public class JsonWrapper<T extends JsonElement> {
    protected T element;

    public JsonWrapper(T element) {
        super();
        this.element = element;
    }

    public T getElement() {
        return element;
    }
    
    public JsonObjectWrapper asObject() throws JsonFormatException {
        if (this.element.isJsonObject()) {
            return new JsonObjectWrapper(this.element.getAsJsonObject());
        } else {
            throw failureByMismatch("object");
        }
    }
    
    public JsonArrayWrapper asArray()  throws JsonFormatException {
        if (this.element.isJsonArray())  {
            return new JsonArrayWrapper(this.element.getAsJsonArray());
        } else {
            throw failureByMismatch("array");
        }
    }
    
    public JsonPrimitiveWrapper asPrimitive() throws JsonFormatException {
        if (this.element.isJsonPrimitive()) {
            return new JsonPrimitiveWrapper(this.element.getAsJsonPrimitive());
        } else {
            throw failureByMismatch("primitive");
        }
    }
    
    public boolean isNull() {
        return this.element.isJsonNull();
    }
    
    public String asString() throws JsonFormatException {
        return this.asPrimitive().asString();
    }
    
    protected JsonFormatException failureByMismatch(String expectation) {
        return failureWithMessage("Expected " + expectation + " but found " + getJsonTypeDescription());
    }
    
    protected JsonFormatException failureWithMessage(String message) {
        return new JsonFormatException(message+": "+this.element);
    }
    
    public String getJsonTypeDescription() {
        if (this.element.isJsonObject()) {
            return "object";
        } else if (this.element.isJsonArray()) {
            return "array";
        } else if (this.element.isJsonNull() || this.element == null) {
            return "null";
        } else if (this.element.isJsonPrimitive()) {
            JsonPrimitive primitive = this.element.getAsJsonPrimitive();
            if (primitive.isBoolean()) {
                return "boolean";
            } else if (primitive.isNumber()) {
                return "number";
            } else if (primitive.isString()) {
                return "string";
            } else {
                return "???";
            }
        } else {
            return "???";
        }
    }

    public static JsonWrapper<?> wrap(JsonElement element) {
        if (element.isJsonObject()) {
            return new JsonObjectWrapper(element.getAsJsonObject());
        } else if (element.isJsonArray()) {
            return new JsonArrayWrapper(element.getAsJsonArray());
        } else if (element.isJsonNull() || element == null) {
            return new JsonNullWrapper(element.getAsJsonNull());
        } else if (element.isJsonPrimitive()) {
            return new JsonPrimitiveWrapper(element.getAsJsonPrimitive());
        } else {
            throw new IllegalStateException("Unrecognized JSON type: " + element.getClass().getName());
        }
    }
}
