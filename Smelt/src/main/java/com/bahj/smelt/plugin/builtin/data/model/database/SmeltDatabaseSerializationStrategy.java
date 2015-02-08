package com.bahj.smelt.plugin.builtin.data.model.database;

import com.bahj.smelt.plugin.builtin.data.model.value.serialization.SmeltValueSerializationStrategyRegistry;
import com.bahj.smelt.plugin.builtin.data.model.value.utils.SmeltValueWrapper;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.bahj.smelt.serialization.json.SmeltJSONSerializationStrategy;
import com.bahj.smelt.util.json.JsonArrayWrapper;
import com.bahj.smelt.util.json.JsonFormatException;
import com.bahj.smelt.util.json.JsonObjectWrapper;
import com.bahj.smelt.util.json.JsonWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SmeltDatabaseSerializationStrategy implements SmeltJSONSerializationStrategy<SmeltDatabase> {
    private static final String TYPE_FIELD = "type";
    private static final String TYPE_NAME = "SmeltDatabase";
    private static final String VALUES_FIELD = "values";

    private SmeltValueSerializationStrategyRegistry registry;

    public SmeltDatabaseSerializationStrategy(SmeltValueSerializationStrategyRegistry registry) {
        this.registry = registry;
    }

    @Override
    public JsonElement objectToJson(SmeltDatabase database) throws SerializationException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TYPE_FIELD, TYPE_NAME);
        JsonArray valueArray = new JsonArray();
        for (SmeltValueWrapper<?> valueWrapper : database.getAllWrapped()) {
            JsonElement valueAsJson = this.registry.serializeValue(valueWrapper);
            valueArray.add(valueAsJson);
        }
        jsonObject.add(VALUES_FIELD, valueArray);
        return jsonObject;
    }

    @Override
    public SmeltDatabase jsonToObject(JsonElement json) throws DeserializationException {
        try {
            JsonObjectWrapper databaseJson = JsonWrapper.wrap(json).asObject();
            if (!databaseJson.getRequiredField(TYPE_FIELD).asString().equals(TYPE_NAME)) {
                throw new DeserializationException(
                        "Provided JSON object was not a database object: type tag incorrect.");
            }
            JsonArrayWrapper valuesArray = databaseJson.getRequiredField(VALUES_FIELD).asArray();

            SmeltDatabase database = new SmeltDatabase();
            for (JsonElement element : valuesArray.getElement()) {
                SmeltValueWrapper<?> valueWrapper = this.registry.deserializeValue(element);
                database.add(valueWrapper.getSmeltValue());
            }
            
            return database;
        } catch (JsonFormatException e) {
            throw new DeserializationException("Provided JSON object was not a correctly-formatted database object.", e);
        }
    }
}
