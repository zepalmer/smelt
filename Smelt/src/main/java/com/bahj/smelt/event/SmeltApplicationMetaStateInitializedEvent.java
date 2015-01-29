package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;

/**
 * An event which is dispatched when the meta-state of the Smelt application is initialized but before the plugins have
 * operated on it.  The meta-state objects (the logical model and the UI model) will be new objects; any listeners
 * which should be attached to them should be attached when this event is received.
 * 
 * @author Zachary Palmer
 */
public class SmeltApplicationMetaStateInitializedEvent extends SmeltApplicationEvent {
    public SmeltApplicationMetaStateInitializedEvent(SmeltApplicationModel applicationModel) {
        super(applicationModel);
    }
}
