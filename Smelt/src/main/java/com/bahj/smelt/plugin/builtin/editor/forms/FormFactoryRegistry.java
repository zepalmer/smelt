package com.bahj.smelt.plugin.builtin.editor.forms;

import java.util.HashMap;
import java.util.Map;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;

/**
 * A registry which stores form factories by the {@link SmeltType} for which they are able to generate forms.
 * 
 * @author Zachary Palmer
 */
public class FormFactoryRegistry {
    private Map<SmeltType<?, ?>, FormFactory> map;

    public FormFactoryRegistry() {
        super();
        this.map = new HashMap<>();
    }

    public FormFactory getFormFactory(SmeltType<?, ?> type) {
        return map.get(type);
    }

    public void registerFormFactory(SmeltType<?, ?> type, FormFactory factory) {
        this.map.put(type, factory);
    }

    public void clear() {
        this.map.clear();
    }

    @Override
    public String toString() {
        return String.valueOf(this.map);
    }
}
