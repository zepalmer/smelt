package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;

/**
 * An event which is dispatched when the specification of the Smelt application is initialized and loaded. By this
 * point, all plugins have processed the declarations of the new specification.
 * 
 * @author Zachary Palmer
 */
public class SmeltApplicationSpecificationLoadedEvent extends SmeltApplicationEvent {
    public SmeltApplicationSpecificationLoadedEvent(SmeltApplicationModel applicationModel) {
        super(applicationModel);
    }
}
