package com.bahj.smelt.plugin.builtin.data.model.value.serialization;

import java.util.Iterator;
import java.util.Map;

import com.bahj.smelt.plugin.builtin.data.model.model.SmeltDataModel;
import com.bahj.smelt.plugin.builtin.data.model.type.DataType;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltDatum;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltDatumEvent;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.json.JsonFormatException;
import com.bahj.smelt.util.json.JsonObjectWrapper;
import com.bahj.smelt.util.json.JsonWrapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SmeltDatumSerializationStrategy extends
        AbstractSmeltValueSerializationStrategy<SmeltDatum, SmeltDatumEvent> {
    private static final String TYPE_NAME_KEY = "typeName";
    private static final String PROPERTY_MAP_KEY = "propertyMap";

    private SmeltDataModel dataModel;
    private SmeltValueSerializationStrategyRegistry registry;

    public SmeltDatumSerializationStrategy(SmeltDataModel dataModel, SmeltValueSerializationStrategyRegistry registry) {
        super(SmeltDatum.class);
        this.dataModel = dataModel;
        this.registry = registry;
    }

    @Override
    protected JsonElement valueToJson(ValueSerializationContext context, SmeltDatum value)
            throws SerializationException {
        JsonObject propertyMap = new JsonObject();
        Iterator<String> fieldIterator = value.fieldNames();
        while (fieldIterator.hasNext()) {
            String fieldName = fieldIterator.next();
            JsonElement fieldJson = this.registry.serializeValue(context, value.getWrapped(fieldName));
            propertyMap.add(fieldName, fieldJson);
        }

        JsonObject datumObject = new JsonObject();
        datumObject.addProperty(TYPE_NAME_KEY, value.getType().getName());
        datumObject.add(PROPERTY_MAP_KEY, propertyMap);
        return datumObject;
    }

    @Override
    protected SmeltDatum jsonToValue(ValueDeserializationContext context, JsonWrapper<?> json)
            throws DeserializationException, JsonFormatException {
        JsonObjectWrapper datumObject = json.asObject();
        String typeName = datumObject.getField(TYPE_NAME_KEY).asString();
        SmeltType<?, ?> smeltType = this.dataModel.getTypes().get(typeName);
        if (!(smeltType instanceof DataType)) {
            throw new NotYetImplementedException();
        }
        SmeltDatum datum = new SmeltDatum((DataType) smeltType);

        JsonObjectWrapper propertyMap = datumObject.getField(PROPERTY_MAP_KEY).asObject();
        for (Map.Entry<String, JsonElement> entry : propertyMap.getElement().entrySet()) {
            String fieldName = entry.getKey();
            datum.set(fieldName, this.registry.deserializeValue(context, entry.getValue()).getSmeltValue());
        }
        return datum;
    }
}
