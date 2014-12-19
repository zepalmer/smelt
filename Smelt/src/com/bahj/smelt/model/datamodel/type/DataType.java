package com.bahj.smelt.model.datamodel.type;

import java.util.HashMap;
import java.util.Map;

import com.bahj.smelt.model.datamodel.value.SmeltDatum;

/**
 * Represents a structured data type in Smelt.
 * 
 * @author Zachary Palmer
 *
 */
public class DataType implements SmeltType<SmeltDatum> {
	private Map<String, SmeltType<?>> properties;

	public DataType() {
		this.properties = new HashMap<>();
	}

	public Map<String, SmeltType<?>> getProperties() {
		return properties;
	}
}
