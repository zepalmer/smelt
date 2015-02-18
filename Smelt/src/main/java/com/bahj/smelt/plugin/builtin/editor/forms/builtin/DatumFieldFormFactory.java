package com.bahj.smelt.plugin.builtin.editor.forms.builtin;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltTypeMismatchException;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltDatum;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.editor.forms.DefaultForm;
import com.bahj.smelt.plugin.builtin.editor.forms.Form;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactory;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactoryRegistry;
import com.bahj.smelt.util.NotYetImplementedException;

public class DatumFieldFormFactory implements FormFactory {
    private FormFactoryRegistry formFactoryRegistry;
    private SmeltType<?, ?> fieldType;
    private String fieldName;
    private int labelSpacing;
    private String fieldDisplayName;

    public DatumFieldFormFactory(FormFactoryRegistry formFactoryRegistry, SmeltType<?, ?> fieldType, String fieldName,
            int labelSpacing) {
        super();
        this.formFactoryRegistry = formFactoryRegistry;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.labelSpacing = labelSpacing;
        this.fieldDisplayName = fieldName;
    }

    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
    }

    @Override
    public Form createForm(SmeltValue<?, ?> value) throws SmeltTypeMismatchException {
        // If the value is not a datum, we should provide an initialization form.
        if (!(value instanceof SmeltDatum)) {
            // TODO: an initialization form (similar to above in the container logic)
            throw new NotYetImplementedException();
        }

        SmeltDatum datum = (SmeltDatum) value;

        // Fetch the value currently contained in that field.
        SmeltValue<?, ?> fieldValue = datum.get(fieldName);

        // Fetch the form factory for this field's type.
        FormFactory factory = this.formFactoryRegistry.getFormFactory(fieldType);
        if (factory == null) {
            throw new NotYetImplementedException("No factory found for field type " + fieldType); // TODO
        }

        // Create the form if possible.
        try {
            Form form = factory.createForm(fieldValue);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(form.getComponent(), BorderLayout.CENTER);
            String fieldDisplay = this.fieldDisplayName;
            JLabel fieldLabel = new JLabel(fieldDisplay, SwingConstants.RIGHT);
            fieldLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, labelSpacing));
            panel.add(fieldLabel, BorderLayout.WEST);
            return new DefaultForm(panel, (Void v) -> {
                form.destroy();
            });
        } catch (SmeltTypeMismatchException e) {
            // TODO
            // In this case, the value in the field does not match the type provided to the form. We
            // should alert the user (and avoid changing anything), but we should also provide the
            // opportunity for the user to e.g. push a button to clear the field and initialize it with
            // a fresh value of the appropriate type.
            throw new NotYetImplementedException();
        }
    }
}