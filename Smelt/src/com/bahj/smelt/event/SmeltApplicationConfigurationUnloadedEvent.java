package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;

public class SmeltApplicationConfigurationUnloadedEvent extends SmeltApplicationEvent {
    public SmeltApplicationConfigurationUnloadedEvent(SmeltApplicationModel model) {
        super(model);
    }
}
