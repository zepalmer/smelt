package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;

public class SmeltApplicationMetaStateUnloadedEvent extends SmeltApplicationEvent {
    public SmeltApplicationMetaStateUnloadedEvent(SmeltApplicationModel applicationModel) {
        super(applicationModel);
    }
}
