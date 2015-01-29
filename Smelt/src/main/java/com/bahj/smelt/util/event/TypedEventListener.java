package com.bahj.smelt.util.event;

/**
 * An event listener proxy which forwards events to the underlying listener only if they match a specific class.
 * 
 * @author Zachary Palmer
 * @param <T>
 *            The type of events <i>before</i> they are filtered.
 */
public class TypedEventListener<U extends T, T extends Event> implements EventListener<T> {
    private Class<U> eventClass;
    private EventListener<? super U> listener;

    public TypedEventListener(Class<U> eventClass, EventListener<? super U> listener) {
        super();
        this.eventClass = eventClass;
        this.listener = listener;
    }

    @Override
    public void eventOccurred(T event) {
        if (this.eventClass.isInstance(event)) {
            U eventAsU = this.eventClass.cast(event);
            this.listener.eventOccurred(eventAsU);
        }
    }
}
