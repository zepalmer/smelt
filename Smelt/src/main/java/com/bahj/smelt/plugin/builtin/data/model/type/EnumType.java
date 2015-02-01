package com.bahj.smelt.plugin.builtin.data.model.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltEnumValue;

/**
 * Represents a structured enumerated type in a Smelt data model.
 * 
 * @author Zachary Palmer
 */
public class EnumType implements SmeltType<SmeltEnumValue> {
    private String name;
	private List<String> choices;

	public EnumType(String name, String... choices) {
		this(Arrays.asList(choices));
	}

	public String getName() {
        return name;
    }

    public EnumType(List<String> choices) {
		this.choices = new ArrayList<>(choices);
	}

	public List<String> getChoices() {
		return choices;
	}

	/**
	 * Instantiates this enumerated type.  The default value for a Smelt enum is <code>null</code>.
	 */
    @Override
    public SmeltEnumValue instantiate() {
        return null;
    }
}
