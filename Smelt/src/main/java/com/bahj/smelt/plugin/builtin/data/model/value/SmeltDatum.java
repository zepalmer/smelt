package com.bahj.smelt.plugin.builtin.data.model.value;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bahj.smelt.plugin.builtin.data.model.type.DataType;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltDatumEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.utils.SmeltValueWrapper;

/**
 * Represents an instance of a Smelt data type. Because the schema for a datum can change after the datum is created, we
 * treat the datum as a general dictionary type mapping field names onto Smelt values. A given datum may contain data
 * which is not reflected by its type or may be missing data which its type would imply; in either case, client code
 * should be prepared to deal with the discrepancy.
 * 
 * @author Zachary Palmer
 *
 */
public class SmeltDatum extends AbstractSmeltValue<SmeltDatum, SmeltDatumEvent> {
    private Map<String, SmeltValueWrapper<?>> properties;

    public SmeltDatum(DataType type) {
        super(type);
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
    public SmeltValue<?> get(String fieldName) {
        SmeltValueWrapper<?> wrapper = this.properties.get(fieldName);
        return (wrapper == null) ? null : wrapper.getSmeltValue();
    }

    /**
     * Retrieves the value of a given field on this record.
     * 
     * @param fieldName
     *            The name of the field in question.
     * @return The value of that field (or <code>null</code> if that field has no value).
     */
    public SmeltValueWrapper<?> getWrapped(String fieldName) {
        return this.properties.get(fieldName);
    }

    /**
     * Updates the value of a given field on this record.
     * 
     * @param fieldName
     *            The field name to update.
     * @param value
     *            The new value for that field (or <code>null</code> to delete it).
     */
    public <V extends SmeltValue<V>> void set(String fieldName, V value) {
        if (value == null) {
            this.properties.remove(fieldName);
        } else {
            this.properties.put(fieldName, new SmeltValueWrapper<>(value));
        }
    }
}
