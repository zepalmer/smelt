package com.bahj.smelt.plugin.builtin.data.type;

import com.bahj.smelt.plugin.builtin.data.value.SmeltString;

/**
 * The primitive Smelt type representing a textual value.
 * @author Zachary Palmer
 */
public class TextType extends PrimitiveType<SmeltString> {
    public static final TextType INSTANCE = new TextType();
    
    public TextType() {
    }
    
    public String getName() {
        return "text";
    }
}
