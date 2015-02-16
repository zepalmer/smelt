package com.bahj.smelt.util.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A generic template for handling event generation. Provides the subclass with an abstract method for event
 * dispatching.
 * 
 * @author Zachary Palmer
 */
public abstract class AbstractEventGenerator<T extends Event> implements EventGenerator<T> {
    private Set<EventListener<? super T>> listeners;

    public AbstractEventGenerator() {
        this.listeners = new HashSet<>();
    }

    @Override
    public void addListener(EventListener<? super T> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener<? super T> listener) {
        this.listeners.remove(listener);
    }

    /**
     * Dispatches the provided event to all listeners.
     * 
     * @param event
     *            The event object to dispatch.
     */
    protected <U extends T> void fireEvent(U event) {
        // NOTE: The type parameter on this function wouldn't be strictly necessary but helps the type inference engine.
        // NOTE: The copy is created below to allow listeners to add listeners (usually for other event types).
        for (EventListener<? super T> listener : new ArrayList<EventListener<? super T>>(listeners)) {
            listener.eventOccurred(event);
        }
    }
}
