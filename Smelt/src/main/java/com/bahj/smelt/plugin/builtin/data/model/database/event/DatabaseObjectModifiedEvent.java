package com.bahj.smelt.plugin.builtin.data.model.database.event;

import com.bahj.smelt.plugin.builtin.data.model.database.SmeltDatabase;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

public class DatabaseObjectModifiedEvent extends DatabaseEvent {
    private SmeltValue<?, ?> value;

    public DatabaseObjectModifiedEvent(SmeltDatabase database, SmeltValue<?, ?> value) {
        super(database);
        this.value = value;
    }

    public SmeltValue<?, ?> getValue() {
        return value;
    }
}
