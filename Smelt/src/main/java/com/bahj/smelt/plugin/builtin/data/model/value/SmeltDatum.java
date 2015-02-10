package com.bahj.smelt.plugin.builtin.data.model.value;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bahj.smelt.plugin.builtin.data.model.type.DataType;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltDatumEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltDatumPropertyChangeEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltDatumPropertyInnerUpdateEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltDatumTitlePropertyUpdateEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueUpdateEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.utils.SmeltValueWrapper;
import com.bahj.smelt.util.event.EventListener;

/**
 * Represents an instance of a Smelt data type. Because the schema for a datum can change after the datum is created, we
 * treat the datum as a general dictionary type mapping field names onto Smelt values. A given datum may contain data
 * which is not reflected by its type or may be missing data which its type would imply; in either case, client code
 * should be prepared to deal with the discrepancy.
 * 
 * @author Zachary Palmer
 */
public class SmeltDatum extends AbstractSmeltValue<SmeltDatum, SmeltDatumEvent> {
    private DataType dataType;
    private Map<String, FieldEntry<?, ?>> properties;

    public SmeltDatum(DataType type) {
        super(type);
        this.dataType = type;
        this.properties = new HashMap<>();
    }

    public Iterator<String> fieldNames() {
        return Collections.unmodifiableMap(this.properties).keySet().iterator();
    }

    /**
     * Retrieves the value of a given field on this record.
     * 
     * @param fieldName
     *            The name of the field in question.
     * @return The value of that field (or <code>null</code> if that field has no value).
     */
    public SmeltValue<?, ?> get(String fieldName) {
        SmeltValueWrapper<?, ?> wrapper = this.properties.get(fieldName).getValueWrapper();
        return (wrapper == null) ? null : wrapper.getSmeltValue();
    }

    /**
     * Retrieves the value of a given field on this record.
     * 
     * @param fieldName
     *            The name of the field in question.
     * @return The value of that field (or <code>null</code> if that field has no value).
     */
    public SmeltValueWrapper<?, ?> getWrapped(String fieldName) {
        return this.properties.get(fieldName).getValueWrapper();
    }

    /**
     * Updates the value of a given field on this record.
     * 
     * @param fieldName
     *            The field name to update.
     * @param value
     *            The new value for that field (or <code>null</code> to delete it).
     */
    public <V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> void set(String fieldName, V value) {
        FieldEntry<?, ?> entry = this.properties.get(fieldName);
        if (entry != null) {
            entry.removePropagatingListenerFromValue();
        }
        if (value == null) {
            this.properties.remove(fieldName);
        } else {
            FieldEntry<V, E> newEntry = new FieldEntry<>(new SmeltValueWrapper<>(value),
                    new FieldUpdatePropagatingListener<>(fieldName));
            this.properties.put(fieldName, newEntry);
            newEntry.addPropagatingListenerToValue();
        }
        fireEvent(new SmeltDatumPropertyChangeEvent(this, fieldName, entry.getValueWrapper().getSmeltValue(), value));
        if (this.dataType.getTitleFieldName().equals(fieldName)) {
            fireEvent(new SmeltDatumTitlePropertyUpdateEvent(this));
        }
    }

    private class FieldEntry<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> {
        private SmeltValueWrapper<V, E> valueWrapper;
        private EventListener<E> propagatingListener;

        public FieldEntry(SmeltValueWrapper<V, E> valueWrapper, EventListener<E> propagatingListener) {
            super();
            this.valueWrapper = valueWrapper;
            this.propagatingListener = propagatingListener;
        }

        public SmeltValueWrapper<V, E> getValueWrapper() {
            return valueWrapper;
        }

        public void addPropagatingListenerToValue() {
            this.valueWrapper.getSmeltValue().addListener(this.propagatingListener);
        }

        public void removePropagatingListenerFromValue() {
            this.valueWrapper.getSmeltValue().removeListener(this.propagatingListener);
        }
    }

    private class FieldUpdatePropagatingListener<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>>
            implements EventListener<E> {
        private String fieldName;

        public FieldUpdatePropagatingListener(String fieldName) {
            super();
            this.fieldName = fieldName;
        }

        @Override
        public void eventOccurred(E event) {
            if (event instanceof SmeltValueUpdateEvent) {
                SmeltValueUpdateEvent<?, ?, ?> updateEvent = (SmeltValueUpdateEvent<?, ?, ?>) event;
                fireEvent(new SmeltDatumPropertyInnerUpdateEvent(SmeltDatum.this, this.fieldName,
                        updateEvent.getValue(), updateEvent));
                if (this.fieldName.equals(SmeltDatum.this.dataType.getTitleFieldName())) {
                    fireEvent(new SmeltDatumTitlePropertyUpdateEvent(SmeltDatum.this));
                }
            }
        }
    }
}
