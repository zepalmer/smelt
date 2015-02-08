package com.bahj.smelt.plugin.builtin.data.model.value.serialization;

import com.bahj.smelt.plugin.builtin.data.model.model.SmeltDataModel;
import com.bahj.smelt.plugin.builtin.data.model.type.EnumType;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltEnumValue;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.json.JsonFormatException;
import com.bahj.smelt.util.json.JsonObjectWrapper;
import com.bahj.smelt.util.json.JsonWrapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SmeltEnumValueSerializationStrategy extends AbstractSmeltValueSerializationStrategy<SmeltEnumValue> {
    private static final String TYPE_NAME_KEY = "typeName";
    private static final String CHOICE_KEY = "choice";

    private SmeltDataModel dataModel;

    public SmeltEnumValueSerializationStrategy(SmeltDataModel dataModel) {
        super(SmeltEnumValue.class);
        this.dataModel = dataModel;
    }

    @Override
    protected JsonElement valueToJson(SmeltEnumValue value) throws SerializationException {
        JsonObject enumValueObject = new JsonObject();
        enumValueObject.addProperty(TYPE_NAME_KEY, value.getType().getName());
        enumValueObject.addProperty(CHOICE_KEY, value.getChoice());
        return enumValueObject;
    }

    @Override
    protected SmeltEnumValue jsonToValue(JsonWrapper<?> json) throws DeserializationException, JsonFormatException {
        JsonObjectWrapper enumValueObject = json.asObject();
        String typeName = enumValueObject.getRequiredField(TYPE_NAME_KEY).asString();
        String choice = enumValueObject.getField(CHOICE_KEY).asString();
        SmeltType<?> smeltType = dataModel.getTypes().get(typeName);
        if (smeltType == null || !(smeltType instanceof EnumType)) {
            throw new NotYetImplementedException(); // TODO
        }
        return new SmeltEnumValue((EnumType) smeltType, choice);
    }
}
