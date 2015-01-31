package com.bahj.smelt.plugin.builtin.data.model.database.event;

import com.bahj.smelt.plugin.builtin.data.model.database.SmeltDatabase;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * Fired when an object is added to a Smelt database.
 * @author Zachary Palmer
 */
public class DatabaseObjectAddedEvent extends DatabaseEvent {
    private SmeltValue<?> value;

    public DatabaseObjectAddedEvent(SmeltDatabase database, SmeltValue<?> value) {
        super(database);
        this.value = value;
    }

    public SmeltValue<?> getValue() {
        return value;
    }
    
}
