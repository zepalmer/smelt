package com.bahj.smelt.model.datamodel;

import java.util.List;

import com.bahj.smelt.model.datamodel.type.SmeltType;

/**
 * Represents the description of a data model in Smelt.
 * @author Zachary Palmer
 */
public class ModelDescription {
	private List<? extends SmeltType<?>> types;

	public ModelDescription(List<? extends SmeltType<?>> types) {
		super();
		this.types = types;
	}

	public List<? extends SmeltType<?>> getTypes() {
		return types;
	}
}
