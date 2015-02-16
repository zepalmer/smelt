package com.bahj.smelt.plugin.builtin.data.model.database.event;

import com.bahj.smelt.plugin.builtin.data.model.database.SmeltDatabase;
import com.bahj.smelt.plugin.builtin.data.model.event.DataModelPluginEvent;

/**
 * The base class for events which occur on a Smelt database.
 * 
 * @author Zachary Palmer
 */
public abstract class DatabaseEvent extends DataModelPluginEvent {
    private SmeltDatabase database;

    public DatabaseEvent(SmeltDatabase database) {
        super();
        this.database = database;
    }

    public SmeltDatabase getDatabase() {
        return database;
    }
}
