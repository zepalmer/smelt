package com.bahj.smelt.plugin.builtin.editor.forms.builtin;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.bahj.smelt.plugin.builtin.data.model.type.EnumType;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltTypeMismatchException;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltEnumValue;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltEnumEvent;
import com.bahj.smelt.plugin.builtin.editor.forms.DefaultForm;
import com.bahj.smelt.plugin.builtin.editor.forms.Form;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactory;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.event.EventListener;

public class EnumFormFactory implements FormFactory {
    @Override
    public Form createForm(SmeltValue<?, ?> value) throws SmeltTypeMismatchException {
        if (!(value instanceof SmeltEnumValue)) {
            throw new NotYetImplementedException(); // TODO: appropriate error
        }
        SmeltEnumValue enumValue = (SmeltEnumValue) value;
        EnumType type = (EnumType) enumValue.getType(); // TODO: add T parameter
                                                        // (along with V and E)
                                                        // to fix this

        // Create a list of options. We always allow null as an option.
        List<Option> options = new ArrayList<>();
        options.add(Option.NULL_CHOICE);
        options.addAll(type.getChoices().stream().map((String s) -> new Option(s, s, false))
                .collect(Collectors.toList()));

        // Find the option matching our choice.
        Option matchingCurrent = null;
        if (enumValue.getChoice() == null) {
            matchingCurrent = Option.NULL_CHOICE;
        } else {
            for (Option option : options) {
                if (enumValue.getChoice().equals(option.getChoice())) {
                    matchingCurrent = option;
                    break;
                }
            }
        }
        if (matchingCurrent == null) {
            // Then this enum value does not appear in the type. Offer it and
            // default to it, but keep it marked.
            matchingCurrent = new Option(enumValue.getChoice(), enumValue.getChoice(), true);
            options.add(0, matchingCurrent);
        }

        // Build the combo box around this model.
        JComboBox<Option> box = new JComboBox<>(new Vector<>(options));
        box.setSelectedItem(matchingCurrent);
        box.setEditable(false);
        box.setRenderer(new ListCellRenderer<Option>() {
            private DefaultListCellRenderer renderer;

            @Override
            public Component getListCellRendererComponent(JList<? extends Option> list, Option value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = this.renderer.getListCellRendererComponent(list, value.getDisplay(), index, isSelected,
                        cellHasFocus);
                if (value.isSpecial() && c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setFont(jc.getFont().deriveFont(Font.ITALIC));
                }
                return c;
            }
        });

        // Update Smelt value when combo box changes.
        box.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Option selectedOption = (Option) box.getSelectedItem();
                if ((selectedOption.getChoice() == null && enumValue.getChoice() != null)
                        || !selectedOption.getChoice().equals(enumValue.getChoice())) {
                    // The selected option has changed; update the value.
                    enumValue.setChoice(selectedOption.getChoice());
                }
            }
        });

        // Update combo box when Smelt value changes.
        EventListener<SmeltEnumEvent> listener = new EventListener<SmeltEnumEvent>() {
            @Override
            public void eventOccurred(SmeltEnumEvent event) {
                Option selectedOption = (Option) box.getSelectedItem();
                if ((selectedOption.getChoice() == null && enumValue.getChoice() != null)
                        || !selectedOption.getChoice().equals(enumValue.getChoice())) {
                    // The value changed out from under us.  Update the value.
                    for (Option option : options) {
                        if ((option == null && enumValue.getChoice() == null) ||
                                option.getChoice().equals(enumValue.getChoice())) {
                            // Found it.
                            box.setSelectedItem(option);
                            break;
                        }
                    }
                }
            }
        };
        enumValue.addListener(listener);

        // Remove the listener when the form is destroyed.
        return new DefaultForm(box, (Void v) -> enumValue.removeListener(listener));
    }

    private static class Option {
        public static final Option NULL_CHOICE = new Option("", null, false);

        private String display;
        private String choice;
        private boolean special;

        public Option(String display, String choice, boolean special) {
            super();
            this.display = display;
            this.choice = choice;
            this.special = special;
        }

        public String getDisplay() {
            return display;
        }

        public String getChoice() {
            return choice;
        }

        public boolean isSpecial() {
            return special;
        }

    }
}
