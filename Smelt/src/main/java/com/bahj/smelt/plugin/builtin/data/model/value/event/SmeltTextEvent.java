package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltText;

public abstract class SmeltTextEvent extends SmeltValueEvent<SmeltText, SmeltTextEvent> {
    public SmeltTextEvent(SmeltText value) {
        super(value);
    }

}
