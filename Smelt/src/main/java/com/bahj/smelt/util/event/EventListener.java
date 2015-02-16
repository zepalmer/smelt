package com.bahj.smelt.util.event;

/**
 * An interface for objects which expect to receive events.
 * 
 * @author Zachary Palmer
 * @param <T>
 *            The type of event that this listener expects to receive.
 */
public interface EventListener<T extends Event> {
    /**
     * Indicates that an event has occurred.
     * 
     * @param event
     *            The event in question.
     */
    public void eventOccurred(T event);
}
