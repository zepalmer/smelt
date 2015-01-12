package com.bahj.smelt.util.event;

/**
 * An interface for objects which generate events.
 * @author Zachary Palmer
 * @param <T> The type of event which is generated.
 */
public interface EventGenerator<T extends Event> {
    public void addListener(EventListener<? super T> listener);
    public void removeListener(EventListener<? super T> listener);
}
