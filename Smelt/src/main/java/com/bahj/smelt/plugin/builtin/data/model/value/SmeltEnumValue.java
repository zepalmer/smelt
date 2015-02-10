package com.bahj.smelt.plugin.builtin.data.model.value;

import com.bahj.smelt.plugin.builtin.data.model.type.EnumType;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltEnumEvent;

public class SmeltEnumValue extends AbstractSmeltValue<SmeltEnumValue, SmeltEnumEvent> {
	private String choice;

	public SmeltEnumValue(EnumType type, String choice) {
        super(type);
        this.choice = choice;
    }

    public String getChoice() {
		return choice;
	}

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
