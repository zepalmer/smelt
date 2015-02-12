package com.bahj.smelt.util.json;

import com.google.gson.JsonPrimitive;

public class JsonPrimitiveWrapper extends JsonWrapper<JsonPrimitive> {
    public JsonPrimitiveWrapper(JsonPrimitive element) {
        super(element);
    }

    public String asString() throws JsonFormatException {
        if (this.element.isString()) {
            return this.element.getAsString();
        } else {
            throw failureByMismatch("string");
        }
    }
    
    public int asInt() throws JsonFormatException {
        if (this.element.isNumber()) {
            try {
                return this.element.getAsInt();
            } catch (NumberFormatException e) {
                throw failureByMismatch("int");
            }
        } else {
            throw failureByMismatch("int");
        }
    }
}
