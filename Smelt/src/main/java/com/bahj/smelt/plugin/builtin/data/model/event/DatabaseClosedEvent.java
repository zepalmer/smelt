package com.bahj.smelt.plugin.builtin.data.model.event;

import com.bahj.smelt.plugin.builtin.data.model.database.SmeltDatabase;

/**
 * An event which is dispatched whenever the database is closed.
 * 
 * @author Zachary Palmer
 */
public class DatabaseClosedEvent extends DataModelPluginEvent {
    private SmeltDatabase database;

    public DatabaseClosedEvent(SmeltDatabase database) {
        super();
        this.database = database;
    }

    /**
     * Retrieves the database which was closed.
     * @return The object representing the database after it was closed.
     */
    public SmeltDatabase getDatabase() {
        return database;
    }
}
