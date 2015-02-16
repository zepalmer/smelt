package com.bahj.smelt.plugin.builtin.editor.forms.builtin;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltTypeMismatchException;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.editor.forms.DefaultForm;
import com.bahj.smelt.plugin.builtin.editor.forms.Form;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactory;

/**
 * A form factory which constructs a container around a number of forms from other factories.
 * 
 * @author Zachary Palmer
 */
public class ContainerFormFactory implements FormFactory {
    public enum Orientation {
        HORIZONTAL, VERTICAL
    }

    private List<FormFactory> factories;
    private Orientation orientation;

    private int spacing;

    /**
     * Creates a new container factory.
     * 
     * @param factories
     *            The factories which make the components of this form.
     * @param orientation
     *            The orientation of this form (vertical or horizontal).
     * @param spacing
     *            The spacing between elements in this form.
     */
    public ContainerFormFactory(List<FormFactory> factories, Orientation orientation, int spacing) {
        this.factories = factories;
        this.orientation = orientation;
        this.spacing = spacing;
    }

    @Override
    public Form createForm(SmeltValue<?, ?> value) throws SmeltTypeMismatchException {
        // There are two types of containers: horizontal and vertical. Build an appropriate component to
        // host the children based on this.
        JPanel panel = new JPanel();
        switch (orientation) {
            case HORIZONTAL:
                panel.setLayout(new GridLayout(1, factories.size(), spacing, 0));
                break;
            case VERTICAL:
                panel.setLayout(new GridLayout(factories.size(), 1, 0, spacing));
                break;
            default:
                throw new IllegalStateException("Unrecognized container orientation: " + orientation);
        }

        // Now create child forms and add them to the container.
        List<Form> forms = new ArrayList<Form>();
        for (FormFactory factory : factories) {
            // TODO: if the form creation fails, perhaps we can do something about it here?
            // create a form which provides a field initialization button and the current contents of
            // the field (if any)?
            // ACTUALLY, we're probably better off having that failover policy handled by the child form itself.
            forms.add(factory.createForm(value));
        }
        for (Form form : forms) {
            panel.add(form.getComponent());
        }

        // Produce a form which uses the resulting panel and will clean up all of the child forms when
        // it is destroyed.
        return new DefaultForm(panel, (Void v) -> {
            for (Form form : forms) {
                form.destroy();
            }
        });
    }
}