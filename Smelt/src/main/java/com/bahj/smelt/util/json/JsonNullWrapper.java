package com.bahj.smelt.util.json;

import com.google.gson.JsonNull;

public class JsonNullWrapper extends JsonWrapper<JsonNull> {
    public JsonNullWrapper(JsonNull element) {
        super(element);
    }
}
