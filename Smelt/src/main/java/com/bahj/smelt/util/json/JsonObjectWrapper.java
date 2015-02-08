package com.bahj.smelt.util.json;

import com.google.gson.JsonObject;

public class JsonObjectWrapper extends JsonWrapper<JsonObject> {
    public JsonObjectWrapper(JsonObject element) {
        super(element);
    }

    public JsonWrapper<?> getField(String name) {
        return JsonWrapper.wrap(this.element.get(name));
    }
    
    public JsonWrapper<?> getRequiredField(String name) throws JsonFormatException {
        if (this.element.get(name) == null){
            throw failureWithMessage("Expected field \"" + name + "\" to be present.");
        }
        return getField(name);
    }
}
