package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;

/**
 * An event which is dispatched when the meta-state of the Smelt application is initialized and loaded.  By this point,
 * all plugins have operated on it.
 * 
 * @author Zachary Palmer
 */
public class SmeltApplicationMetaStateLoadedEvent extends SmeltApplicationEvent {
    public SmeltApplicationMetaStateLoadedEvent(SmeltApplicationModel applicationModel) {
        super(applicationModel);
    }
}
