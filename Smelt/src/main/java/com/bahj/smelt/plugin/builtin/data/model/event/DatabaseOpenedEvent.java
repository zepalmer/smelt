package com.bahj.smelt.plugin.builtin.data.model.event;

import com.bahj.smelt.plugin.builtin.data.model.database.SmeltDatabase;

/**
 * An event which is dispatched whenever a database is opened.
 * 
 * @author Zachary Palmer
 */
public class DatabaseOpenedEvent extends DataModelPluginEvent {
    private SmeltDatabase database;

    public DatabaseOpenedEvent(SmeltDatabase database) {
        super();
        this.database = database;
    }

    /**
     * Retrieves the database which was opened.
     * @return The database.
     */
    public SmeltDatabase getDatabase() {
        return database;
    }
}
