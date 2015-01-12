package com.bahj.smelt.model.datamodel.type;

import com.bahj.smelt.model.datamodel.value.SmeltValue;

/**
 * An abstract representation of a primitive Smelt data type.
 * 
 * @author Zachary Palmer
 * @param <V>
 *            The Java representation of a value of this type.
 */
public abstract class PrimitiveType<V extends SmeltValue> implements SmeltType<V> {

}
