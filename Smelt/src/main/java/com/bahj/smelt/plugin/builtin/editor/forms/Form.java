package com.bahj.smelt.plugin.builtin.editor.forms;

import javax.swing.JComponent;

/**
 * An interface which must be implemented by a Smelt UI form.
 * 
 * @author Zachary Palmer
 */
public interface Form {
    /**
     * Retrieves the {@link JComponent} representing the GUI content of this form.
     */
    public JComponent getComponent();

    /**
     * Detaches this form from all Smelt resources. Generally, the implementation of this method removes listeners and
     * the like from the Smelt database or value objects. The intent of this method is to allow garbage collection of
     * the form to occur; it is only called when the component will no longer be displayed.
     */
    public void destroy();
}
