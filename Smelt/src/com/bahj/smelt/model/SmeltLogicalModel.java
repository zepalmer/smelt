package com.bahj.smelt.model;

import java.util.HashSet;
import java.util.Set;

import com.bahj.smelt.model.datamodel.SmeltDataModel;
import com.bahj.smelt.model.event.SmeltModelEvent;
import com.bahj.smelt.util.event.EventGenerator;
import com.bahj.smelt.util.event.EventListener;

/**
 * This class represents the backing logic of a Smelt application.  It contains no UI details; it exclusively contains
 * a description of the data model and the elements of data in the database.  When constructed, this model contains an
 * initialized data model (with no declared data types).  The databases for this data model may be loaded and unloaded
 * via the methods on this object.
 * @author Zachary Palmer
 *
 */
public class SmeltLogicalModel implements EventGenerator<SmeltModelEvent> {
    private Set<EventListener<? super SmeltModelEvent>> listeners;

    private SmeltDataModel dataModel;

    public SmeltLogicalModel() {
        super();

        this.listeners = new HashSet<>();
        this.dataModel = new SmeltDataModel();
        // TODO: this.database = null;
    }
    
    /**
     * Retrieves the data model being used for this logical Smelt model.
     * @return The data model that this logical model is using.
     */
    public SmeltDataModel getDataModel() {
        return dataModel;
    }

    /**
     * Unloads the current database.
     */
    public void unloadDatabase() {
        // TODO
    }

    @Override
    public void addListener(EventListener<? super SmeltModelEvent> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener<? super SmeltModelEvent> listener) {
        this.listeners.remove(listener);
    }

    protected void fireEvent(SmeltModelEvent event) {
        for (EventListener<? super SmeltModelEvent> listener : listeners) {
            listener.eventOccurred(event);
        }
    }
}
