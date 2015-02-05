package com.bahj.smelt.plugin.builtin.editor.forms;

import java.util.function.Consumer;

import javax.swing.JComponent;

public class DefaultForm extends AbstractForm {
    private Consumer<Void> cleanup;

    public DefaultForm(JComponent component, Consumer<Void> cleanup) {
        super(component);
        this.cleanup = cleanup;
    }

    @Override
    public void destroy() {
        cleanup.accept(null);
    }
}
