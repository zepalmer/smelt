package com.bahj.smelt.plugin.builtin.editor.forms;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltTypeMismatchException;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * An interface implemented by form factories which produce editing forms for Smelt values. A form factory creates a
 * Swing component for a given Smelt value. In general, form factories are dictated by the type of value being edited;
 * all values of a given type are expected to use the same form.
 * 
 * @author Zachary Palmer
 */
public interface FormFactory {
    /**
     * Creates an appropriate form for the specified Smelt value. The created form object will, as its UI elements
     * change, update the value in the database. It will also update its own UI elements if the database value changes.
     * This synchronization will proceed until {@link Form#destroy()} is called.
     * 
     * @param value
     *            The value to edit.
     * @return The form to use in editing that value.
     * @throws SmeltTypeMismatchException
     *             If the provided value is not of a type editable by this form.
     */
    public Form createForm(SmeltValue<?, ?> value) throws SmeltTypeMismatchException;
}
