package com.bahj.smelt.plugin.builtin.data.model.value;

import java.util.Objects;

import com.bahj.smelt.plugin.builtin.data.model.type.EnumType;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltEnumEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltEnumUpdateEvent;

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
        String oldChoice = this.choice;
        this.choice = choice;
        if (!Objects.equals(this.choice, oldChoice)) {
            fireEvent(new SmeltEnumUpdateEvent(this, oldChoice, choice));
        }
    }

    @Override
    public String getDescription() {
        return this.choice;
    }
}
