package com.bahj.smelt.plugin.builtin.editor;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;

/**
 * A context object provided to clients of the {@link EditorPanel} to allow them to issue instructions.
 * 
 * @author Zachary Palmer
 */
public interface EditorModel {
    /**
     * Opens an editor for the provided Smelt value. If an editor already exists, it is focused.
     * 
     * @param value
     *            The value to edit.
     */
    public <V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> void openEditor(V value);
}
