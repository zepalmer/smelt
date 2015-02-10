package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltDatum;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

public class SmeltDatumPropertyInnerUpdateEvent extends SmeltDatumEvent implements SmeltValueUpdateEvent<SmeltDatum, SmeltDatumEvent, SmeltValue<?,?>>{
    private String fieldName;
    private SmeltValue<?,?> propertyValue;
    private SmeltValueUpdateEvent<?, ?, ?> propertyValueUpdateEvent;

    public SmeltDatumPropertyInnerUpdateEvent(SmeltDatum value, String fieldName, SmeltValue<?, ?> propertyValue,
            SmeltValueUpdateEvent<?, ?, ?> propertyValueUpdateEvent) {
        super(value);
        this.fieldName = fieldName;
        this.propertyValue = propertyValue;
        this.propertyValueUpdateEvent = propertyValueUpdateEvent;
    }

    @Override
    public SmeltValue<?, ?> getOldValue() {
        return propertyValue;
    }

    @Override
    public SmeltValue<?, ?> getNewValue() {
        return propertyValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SmeltValueUpdateEvent<?, ?, ?> getPropertyValueUpdateEvent() {
        return propertyValueUpdateEvent;
    }
}
