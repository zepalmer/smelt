package com.bahj.smelt.plugin.builtin.data.model.value.serialization;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.utils.SmeltValueWrapper;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.bahj.smelt.util.json.JsonFormatException;
import com.bahj.smelt.util.json.JsonObjectWrapper;
import com.bahj.smelt.util.json.JsonWrapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * A convenient foundation for a {@link SmeltValueSerializationStrategy}. This mechanism provides a JSON container for
 * serialization strategies which includes information about the type of {@link SmeltValue} which was serialized.
 * 
 * @author Zachary Palmer
 */
public abstract class AbstractSmeltValueSerializationStrategy<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V,E>>
        implements SmeltValueSerializationStrategy {
    private static final String CLASS_KEY = "smeltValueClass";
    private static final String VALUE_KEY = "smeltValue";

    private Class<V> valueClass;

    /**
     * Creates a new serializations strategy object.
     * 
     * @param valueClass
     *            The type of value class this strategy is able to serialize.
     */
    public AbstractSmeltValueSerializationStrategy(Class<V> valueClass) {
        super();
        this.valueClass = valueClass;
    }

    @Override
    public JsonElement objectToJson(SmeltValueWrapper<?, ?> obj) throws SerializationException {
        if (this.valueClass.isInstance(obj.getSmeltValue())) {
            V value = this.valueClass.cast(obj.getSmeltValue());
            JsonObject wrapperObject = new JsonObject();
            wrapperObject.addProperty(CLASS_KEY, this.valueClass.getName());
            JsonElement valueJson = valueToJson(value);
            wrapperObject.add(VALUE_KEY, valueJson);
            return wrapperObject;
        } else {
            throw new SerializationException("Serializer for " + this.valueClass.getName()
                    + " cannot serialize value of class " + obj.getSmeltValue().getClass());
        }
    }

    @Override
    public SmeltValueWrapper<?, ?> jsonToObject(JsonElement jsonElement) throws DeserializationException {
        try {
            JsonWrapper<?> json = JsonWrapper.wrap(jsonElement);
            JsonObjectWrapper wrapperObject = json.asObject();
            String className = wrapperObject.getRequiredField(CLASS_KEY).asString();
            if (!this.valueClass.getName().equals(className)) {
                throw new DeserializationException("This deserializer handles objects of type "
                        + this.valueClass.getName() + "; the provided object is a serialized " + className);
            }
            JsonWrapper<?> valueJson = wrapperObject.getRequiredField(VALUE_KEY);
            return new SmeltValueWrapper<>(jsonToValue(valueJson));
        } catch (JsonFormatException e) {
            throw new DeserializationException("Deserialization failed: incorrectly formatted JSON", e);
        }
    }

    protected abstract JsonElement valueToJson(V value) throws SerializationException;

    protected abstract V jsonToValue(JsonWrapper<?> json) throws DeserializationException, JsonFormatException;
}
