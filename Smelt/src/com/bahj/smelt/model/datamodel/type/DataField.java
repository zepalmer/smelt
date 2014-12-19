package com.bahj.smelt.model.datamodel.type;

import com.bahj.smelt.model.datamodel.value.SmeltValue;

public class DataField<V extends SmeltValue> {
	private SmeltType<V> fieldType;
	private V value;

	public DataField(SmeltType<V> fieldType, V value) {
		super();
		this.fieldType = fieldType;
		this.value = value;
	}

	public SmeltType<V> getFieldType() {
		return fieldType;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}
