package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;

/**
 * An event dispatched when the current Smelt specification is unloaded from the application.  Plugins are expected to
 * discard any information they have regarding the old specification.
 * @author Zachary Palmer
 */
public class SmeltApplicationSpecificationUnloadedEvent extends SmeltApplicationEvent {
    public SmeltApplicationSpecificationUnloadedEvent(SmeltApplicationModel applicationModel) {
        super(applicationModel);
    }
}
