package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;

/**
 * An event which is dispatched when the specification of the Smelt application is initialized. Plugin are expected to
 * discard any existing specification data and initialize new state in preparation for receiving declarations.
 * 
 * @author Zachary Palmer
 */
public class SmeltApplicationSpecificationInitializedEvent extends SmeltApplicationEvent {
    public SmeltApplicationSpecificationInitializedEvent(SmeltApplicationModel applicationModel) {
        super(applicationModel);
    }
}
