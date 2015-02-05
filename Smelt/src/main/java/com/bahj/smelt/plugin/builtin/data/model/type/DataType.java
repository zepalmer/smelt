package com.bahj.smelt.plugin.builtin.data.model.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltDatum;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * Represents a structured data type in Smelt.
 * 
 * @author Zachary Palmer
 */
public class DataType extends AbstractSmeltType<SmeltDatum> {
    private String name;
    private Map<String, SmeltType<?>> properties;

    public DataType(String name, Map<String, SmeltType<?>> properties) {
        this.name = name;
        this.properties = new HashMap<>(properties);
    }

    public String getName() {
        return name;
    }

    public Map<String, SmeltType<?>> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Instantiates a default value for this data type. This will be a datum which contains default values in all of its
     * declared fields.
     */
    @Override
    public SmeltDatum instantiate() {
        SmeltDatum datum = new SmeltDatum(this);
        for (Map.Entry<String, SmeltType<?>> property : properties.entrySet()) {
            datum.set(property.getKey(), property.getValue().instantiate());
        }
        return datum;
    }

    @Override
    public SmeltDatum coerce(SmeltValue<?> value) throws SmeltTypeMismatchException {
        if (value instanceof SmeltDatum && (value.getType().equals(this))) {
            return (SmeltDatum) value;
        } else {
            throw new SmeltTypeMismatchException(this, value);
        }
    }
}
