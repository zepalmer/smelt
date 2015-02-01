package com.bahj.smelt.plugin.builtin.data.model.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bahj.smelt.plugin.builtin.data.model.database.event.DatabaseEvent;
import com.bahj.smelt.plugin.builtin.data.model.database.event.DatabaseObjectAddedEvent;
import com.bahj.smelt.plugin.builtin.data.model.database.event.DatabaseObjectRemovedEvent;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.util.event.AbstractEventGenerator;

/**
 * An object representing a Smelt database.  Objects are stored indexed by type.
 * @author Zachary Palmer
 */
public class SmeltDatabase extends AbstractEventGenerator<DatabaseEvent> {
    // This class is delicate: it represents a dependently typed index over a collection of Smelt values.  Take care in
    // modifying or extending.
    
    private Map<SmeltType<?>,Set<? extends SmeltValue<?>>> data;
    
    public SmeltDatabase() {
        this.data = new HashMap<>();
    }
    
    private <T extends SmeltValue<T>> Set<T> getValueSetByType(SmeltType<T> type) {
        if (this.data.containsKey(type)) {
            @SuppressWarnings("unchecked")
            Set<T> objects = (Set<T>)this.data.get(type);
            return objects;
        } else {
            Set<T> objects = new HashSet<>();
            this.data.put(type, objects);
            return objects;
        }
    }
    
    public <T extends SmeltValue<T>> void add(T value) {
        getValueSetByType(value.getType()).add(value);
        fireEvent(new DatabaseObjectAddedEvent(this, value));
    }
    
    public <T extends SmeltValue<T>> void remove(T value) {
        getValueSetByType(value.getType()).remove(value);
        fireEvent(new DatabaseObjectRemovedEvent(this, value));
    }
    
    public <T extends SmeltType<V>, V extends SmeltValue<V>> Set<V> getAllOfType(T type) {
        return Collections.unmodifiableSet(getValueSetByType(type));
    }
}
