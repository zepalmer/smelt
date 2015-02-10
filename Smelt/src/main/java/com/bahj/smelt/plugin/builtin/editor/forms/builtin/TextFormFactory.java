package com.bahj.smelt.plugin.builtin.editor.forms.builtin;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltTypeMismatchException;
import com.bahj.smelt.plugin.builtin.data.model.type.TextType;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltText;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltTextEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltTextUpdateEvent;
import com.bahj.smelt.plugin.builtin.editor.forms.Form;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactory;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;

public class TextFormFactory implements FormFactory {
    public static final TextFormFactory INSTANCE = new TextFormFactory();

    private TextFormFactory() {
    }

    @Override
    public Form createForm(SmeltValue<?,?> value) throws SmeltTypeMismatchException {
        SmeltText textValue = TextType.INSTANCE.coerce(value);

        // Create the component.
        // TODO: perhaps the form can specify the appropriate text size?
        JTextField textField = new JTextField(20);
        textField.setText(textValue.getValue());

        // Create the listener which updates the value from the component.
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                contentsChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                contentsChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                contentsChanged();
            }

            private void contentsChanged() {
                textValue.setValue(textField.getText());
            }
        });

        // Create the listener which updates the component from the value.
        EventListener<SmeltTextEvent> listener = new TypedEventListener<>(SmeltTextUpdateEvent.class,
                new EventListener<SmeltTextUpdateEvent>() {
                    @Override
                    public void eventOccurred(SmeltTextUpdateEvent event) {
                        // The check is necessary to prevent modification of the document if this event was fired as a
                        // result of an update from this text field.
                        if (!textField.getText().equals(event.getNewValue())) {
                            textField.setText(event.getNewValue());
                        }
                    }
                });
        textValue.addListener(listener);

        // Now return the resulting form. This form must detach the data listener when it is
        // deleted.
        return new Form() {
            @Override
            public JComponent getComponent() {
                return textField;
            }

            @Override
            public void destroy() {
                textValue.removeListener(listener);
            }
        };
    }
}