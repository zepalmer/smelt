package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltEnumValue;

/**
 * An event generated when an enum value is updated.
 * 
 * @author Zachary Palmer
 */
public class SmeltEnumUpdateEvent extends SmeltEnumEvent {
    private String oldChoice;
    private String newChoice;

    public SmeltEnumUpdateEvent(SmeltEnumValue value, String oldChoice, String newChoice) {
        super(value);
        this.oldChoice = oldChoice;
        this.newChoice = newChoice;
    }

    public String getOldChoice() {
        return oldChoice;
    }

    public String getNewChoice() {
        return newChoice;
    }

}
