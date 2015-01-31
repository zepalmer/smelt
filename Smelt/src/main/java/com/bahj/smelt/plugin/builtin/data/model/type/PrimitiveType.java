package com.bahj.smelt.plugin.builtin.data.model.type;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * An abstract representation of a primitive Smelt data type.
 * 
 * @author Zachary Palmer
 * @param <V>
 *            The Java representation of a value of this type.
 */
public abstract class PrimitiveType<V extends SmeltValue<V>> implements SmeltType<V> {

}
