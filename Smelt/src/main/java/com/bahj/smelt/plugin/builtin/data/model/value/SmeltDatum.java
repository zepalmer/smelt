package com.bahj.smelt.plugin.builtin.data.model.value;

import java.util.HashMap;
import java.util.Map;

import com.bahj.smelt.plugin.builtin.data.model.type.DataType;

public class SmeltDatum extends AbstractSmeltValue<SmeltDatum> {
	private Map<String, SmeltValue<?>> properties;
	private Map<String, String> unparsedProperties;
	
	public SmeltDatum(DataType type) {
	    super(type);
		this.properties = new HashMap<>();
		this.unparsedProperties = new HashMap<>();
	}

	public Map<String, SmeltValue<?>> getProperties() {
		return properties;
	}

	public Map<String, String> getUnparsedProperties() {
		return unparsedProperties;
	}
}
