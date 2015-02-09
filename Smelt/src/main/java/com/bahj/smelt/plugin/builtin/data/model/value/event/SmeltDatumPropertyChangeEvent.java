package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltDatum;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * Fired when the field of a Smelt datum is changed directly.
 * 
 * @author Zachary Palmer
 */
public class SmeltDatumPropertyChangeEvent extends SmeltDatumEvent implements
        SmeltValueUpdateEvent<SmeltDatum, SmeltValue<?>> {
    private String fieldName;
    private SmeltValue<?> oldValue;
    private SmeltValue<?> newValue;

    public SmeltDatumPropertyChangeEvent(SmeltDatum value, String fieldName, SmeltValue<?> oldValue,
            SmeltValue<?> newValue) {
        super(value);
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SmeltValue<?> getOldValue() {
        return oldValue;
    }

    public SmeltValue<?> getNewValue() {
        return newValue;
    }
}
