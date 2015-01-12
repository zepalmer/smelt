package com.bahj.smelt.model.datamodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bahj.smelt.model.datamodel.event.SmeltDataModelEvent;
import com.bahj.smelt.model.datamodel.type.SmeltType;
import com.bahj.smelt.model.datamodel.type.TextType;
import com.bahj.smelt.util.event.EventGenerator;
import com.bahj.smelt.util.event.EventListener;

/**
 * Represents the description of a data model in Smelt. This data model is initialized with the primitive Smelt types.
 * 
 * @author Zachary Palmer
 */
public class SmeltDataModel implements EventGenerator<SmeltDataModelEvent> {
    private Set<EventListener<? super SmeltDataModelEvent>> listeners;

    private Map<String, SmeltType<?>> types;

    public SmeltDataModel() {
        super();
        this.listeners = new HashSet<>();
        this.types = new HashMap<>();

        try {
            this.addType(TextType.INSTANCE);
        } catch (DuplicateTypeNameException e) {
            // Impossible!
            throw new IllegalStateException("Should not be possible during data model initialization!", e);
        }
    }

    public void addType(SmeltType<?> type) throws DuplicateTypeNameException {
        SmeltType<?> existing = this.types.get(type.getName());
        if (existing != null) {
            throw new DuplicateTypeNameException(existing.getName(), existing, type);
        }
        this.types.put(type.getName(), type);
    }

    public Map<String, SmeltType<?>> getTypes() {
        return types;
    }

    @Override
    public void addListener(EventListener<? super SmeltDataModelEvent> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener<? super SmeltDataModelEvent> listener) {
        this.listeners.remove(listener);
    }

    protected void fireEvent(SmeltDataModelEvent event) {
        for (EventListener<? super SmeltDataModelEvent> listener : listeners) {
            listener.eventOccurred(event);
        }
    }
}
