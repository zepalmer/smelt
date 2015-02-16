package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.util.event.Event;

public abstract class SmeltApplicationEvent implements Event {
    private SmeltApplicationModel applicationModel;

    public SmeltApplicationEvent(SmeltApplicationModel applicationModel) {
        super();
        this.applicationModel = applicationModel;
    }

    public SmeltApplicationModel getApplicationModel() {
        return applicationModel;
    }

}
