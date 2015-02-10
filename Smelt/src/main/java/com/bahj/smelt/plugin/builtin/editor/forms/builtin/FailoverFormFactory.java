package com.bahj.smelt.plugin.builtin.editor.forms.builtin;

import java.util.Arrays;
import java.util.List;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltTypeMismatchException;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.editor.forms.Form;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactory;

/**
 * A form factory which is used to allow the failover of form factories. The factories are used in sequence until one of
 * them correctly constructs a form.
 * 
 * @author Zachary Palmer
 */
public class FailoverFormFactory implements FormFactory {
    private List<FormFactory> factories;

    public FailoverFormFactory(FormFactory... factories) {
        this(Arrays.asList(factories));
    }

    public FailoverFormFactory(List<FormFactory> factories) {
        super();
        if (this.factories.size() == 0) {
            throw new IllegalStateException("Failover form factory must have at least one factory.");
        }
        this.factories = factories;
    }

    @Override
    public Form createForm(SmeltValue<?,?> value) throws SmeltTypeMismatchException {
        SmeltTypeMismatchException exception = null;
        for (FormFactory factory : this.factories) {
            try {
                return factory.createForm(value);
            } catch (SmeltTypeMismatchException e) {
                exception = e;
            }
        }
        throw exception;
    }
}
