package com.bahj.smelt.model.datamodel.value;

import java.util.HashMap;
import java.util.Map;

import com.bahj.smelt.model.datamodel.type.DataType;

public class SmeltDatum implements SmeltValue {
	private Map<String, SmeltValue> properties;
	private Map<String, String> unparsedProperties;
	private DataType type;
	
	public SmeltDatum(DataType type) {
		this.properties = new HashMap<>();
		this.unparsedProperties = new HashMap<>();
		this.type = type;
	}

	public Map<String, SmeltValue> getProperties() {
		return properties;
	}

	public Map<String, String> getUnparsedProperties() {
		return unparsedProperties;
	}

	public DataType getType() {
		return type;
	}
}
