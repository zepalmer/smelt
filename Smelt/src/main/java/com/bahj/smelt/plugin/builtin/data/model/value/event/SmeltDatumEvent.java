package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltDatum;

public class SmeltDatumEvent extends SmeltValueEvent<SmeltDatum> {
    public SmeltDatumEvent(SmeltDatum value) {
        super(value);
    }
}