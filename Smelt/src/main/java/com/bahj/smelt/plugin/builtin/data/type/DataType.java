package com.bahj.smelt.plugin.builtin.data.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bahj.smelt.plugin.builtin.data.value.SmeltDatum;

/**
 * Represents a structured data type in Smelt.
 * 
 * @author Zachary Palmer
 *
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
}
