package com.bahj.smelt.plugin.builtin.data.model.value.serialization;

import com.bahj.smelt.plugin.builtin.data.model.type.TextType;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltText;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltTextEvent;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.bahj.smelt.util.json.JsonFormatException;
import com.bahj.smelt.util.json.JsonWrapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class SmeltTextSerializationStrategy extends AbstractSmeltValueSerializationStrategy<SmeltText,SmeltTextEvent> {
    public SmeltTextSerializationStrategy() {
        super(SmeltText.class);
    }

    @Override
    protected JsonElement valueToJson(SmeltText value) throws SerializationException {
        return new JsonPrimitive(value.getValue());
    }

    @Override
    protected SmeltText jsonToValue(JsonWrapper<?> json) throws DeserializationException, JsonFormatException {
        return new SmeltText(TextType.INSTANCE, json.asString());
    }
}
