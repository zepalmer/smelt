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
}
