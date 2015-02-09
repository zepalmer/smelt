package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltText;

/**
 * An event raised when the contents of a {@link SmeltText} are updated.
 * 
 * @author Zachary Palmer
 */
public class SmeltTextUpdateEvent extends SmeltTextEvent implements SmeltValueUpdateEvent<SmeltText, String> {
    private String oldValue;
    private String newValue;

    public SmeltTextUpdateEvent(SmeltText value, String oldValue, String newValue) {
        super(value);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
