package com.bahj.smelt.plugin.builtin.data.model;

import java.util.HashMap;
import java.util.Map;

import com.bahj.smelt.plugin.builtin.data.event.DataModelEvent;
import com.bahj.smelt.plugin.builtin.data.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.type.TextType;
import com.bahj.smelt.util.event.AbstractEventGenerator;

/**
 * Represents the description of a data model in Smelt. This data model is initialized with the primitive Smelt types.
 * 
 * @author Zachary Palmer
 */
public class SmeltDataModel extends AbstractEventGenerator<DataModelEvent> {
    private Map<String, SmeltType<?>> types;

    public SmeltDataModel() {
        super();
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
}
