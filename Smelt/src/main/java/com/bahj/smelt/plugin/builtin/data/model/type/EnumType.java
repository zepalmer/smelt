package com.bahj.smelt.plugin.builtin.data.model.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltEnumValue;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltEnumEvent;

/**
 * Represents a structured enumerated type in a Smelt data model.
 * 
 * @author Zachary Palmer
 */
public class EnumType extends AbstractSmeltType<SmeltEnumValue, SmeltEnumEvent> {
    private String name;
    private List<String> choices;

    public EnumType(String name, String... choices) {
        this(Arrays.asList(choices));
    }

    public String getName() {
        return name;
    }

    public EnumType(List<String> choices) {
        this.choices = new ArrayList<>(choices);
    }

    public List<String> getChoices() {
        return choices;
    }

    /**
     * Instantiates this enumerated type. The default value for a Smelt enum is <code>null</code>.
     */
    @Override
    public SmeltEnumValue instantiate() {
        return null;
    }

    @Override
    public SmeltEnumValue coerce(SmeltValue<?, ?> value) throws SmeltTypeMismatchException {
        if (value instanceof SmeltEnumValue && value.getType().equals(this)) {
            return (SmeltEnumValue) value;
        } else {
            throw new SmeltTypeMismatchException(this, value);
        }
    }
}
