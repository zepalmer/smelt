package com.bahj.smelt.plugin.builtin.data.model.type;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltText;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * The primitive Smelt type representing a textual value.
 * @author Zachary Palmer
 */
public class TextType extends PrimitiveType<SmeltText> {
    public static final TextType INSTANCE = new TextType();
    
    private TextType() {
    }
    
    public String getName() {
        return "text";
    }

    /**
     * Creates a new default string.
     */
    @Override
    public SmeltText instantiate() {
        return new SmeltText(this, "");
    }

    @Override
    public SmeltText coerce(SmeltValue<?> value) throws SmeltTypeMismatchException {
        if (value instanceof SmeltText) {
            return (SmeltText)value;
        } else {
            throw new SmeltTypeMismatchException(this, value);
        }
    }
}
