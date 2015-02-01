package com.bahj.smelt.plugin.builtin.data.model.database.event;

import com.bahj.smelt.plugin.builtin.data.model.database.SmeltDatabase;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.utils.SmeltValueWrapper;

/**
 * Fired when an object is removed from a Smelt database.
 * @author Zachary Palmer
 */
public class DatabaseObjectRemovedEvent extends DatabaseEvent{
    private SmeltValueWrapper<?> wrapper;

    public <V extends SmeltValue<V>> DatabaseObjectRemovedEvent(SmeltDatabase database, V value) {
        super(database);
        this.wrapper = new SmeltValueWrapper<V>(value);
    }

    public SmeltValueWrapper<?> getWrapper() {
        return wrapper;
    }
}
