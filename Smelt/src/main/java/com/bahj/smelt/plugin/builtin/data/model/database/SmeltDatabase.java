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
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.utils.SmeltValueWrapper;
import com.bahj.smelt.util.event.AbstractEventGenerator;

/**
 * An object representing a Smelt database. Objects are stored indexed by type.
 * 
 * @author Zachary Palmer
 */
public class SmeltDatabase extends AbstractEventGenerator<DatabaseEvent> {
    // This class is delicate: it represents a dependently typed index over a collection of Smelt values. Take care in
    // modifying or extending.

    private Map<SmeltType<?, ?>, Set<? extends SmeltValue<?, ?>>> data;
    private Set<SmeltValueWrapper<?, ?>> valueWrappers;

    public SmeltDatabase() {
        this.data = new HashMap<>();
        this.valueWrappers = new HashSet<>();
    }

    private <V extends SmeltValue<V, E>, E extends SmeltValueEvent<V,E>> Set<V> getValueSetByType(SmeltType<V, E> type) {
        if (this.data.containsKey(type)) {
            @SuppressWarnings("unchecked")
            Set<V> objects = (Set<V>) this.data.get(type);
            return objects;
        } else {
            Set<V> objects = new HashSet<>();
            this.data.put(type, objects);
            return objects;
        }
    }

    public <V extends SmeltValue<V, E>, E extends SmeltValueEvent<V,E>> void add(V value) {
        getValueSetByType(value.getType()).add(value);
        this.valueWrappers.add(new SmeltValueWrapper<>(value));
        fireEvent(new DatabaseObjectAddedEvent(this, value));
    }

    public <V extends SmeltValue<V, E>, E extends SmeltValueEvent<V,E>> void remove(V value) {
        getValueSetByType(value.getType()).remove(value);
        this.valueWrappers.remove(new SmeltValueWrapper<>(value));
        fireEvent(new DatabaseObjectRemovedEvent(this, value));
    }

    public <T extends SmeltType<V, E>, V extends SmeltValue<V, E>, E extends SmeltValueEvent<V,E>> Set<V> getAllOfType(
            T type) {
        return Collections.unmodifiableSet(getValueSetByType(type));
    }

    public Set<SmeltValue<?,?>> getAll() {
        Set<SmeltValue<?,?>> values = new HashSet<>();
        getAllWrapped().forEach((SmeltValueWrapper<?,?> w) -> {
            values.add(w.getSmeltValue());
        });
        return values;
    }

    public Set<SmeltValueWrapper<?, ?>> getAllWrapped() {
        return Collections.unmodifiableSet(this.valueWrappers);
    }
}
