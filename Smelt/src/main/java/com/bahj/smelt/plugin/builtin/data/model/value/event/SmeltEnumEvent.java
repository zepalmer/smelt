package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltEnumValue;

public class SmeltEnumEvent extends AbstractSmeltValueEvent<SmeltEnumValue, SmeltEnumEvent> {
    public SmeltEnumEvent(SmeltEnumValue value) {
        super(value);
    }
}
