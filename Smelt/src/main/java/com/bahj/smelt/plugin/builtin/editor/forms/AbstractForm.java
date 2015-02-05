package com.bahj.smelt.plugin.builtin.editor.forms;

import javax.swing.JComponent;

/**
 * This wrapper type exists solely to represent a type which is both a {@link JComponent} and a {@link Form}.
 * 
 * @author Zachary Palmer
 */
public abstract class AbstractForm implements Form {
    private JComponent component;

    public AbstractForm(JComponent component) {
        super();
        this.component = component;
    }

    public JComponent getComponent() {
        return component;
    }
}
