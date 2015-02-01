package com.bahj.smelt.plugin.builtin.data.model.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltDatum;

/**
 * Represents a structured data type in Smelt.
 * 
 * @author Zachary Palmer
 */
public class DataType implements SmeltType<SmeltDatum> {
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
            datum.getProperties().put(property.getKey(), property.getValue().instantiate());
        }
        return datum;
    }
}
