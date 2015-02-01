package com.bahj.smelt.plugin.builtin.data.model.value.utils;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * This wrapper object exists solely for the sake of the Java generics system. It allows a wildcard to be captured and
 * used in two locations. A client can create a <code>SmeltValueWrapper&lt;?&gt;</code> in order to represent a Smelt
 * value of some unknown type. This is necessary because there is no guarantee (from a typing perspective) that some
 * value of type <code>SmeltValue&lt;?&gt;</code> has its own type in its type parameter.
 * 
 * @author Zachary Palmer
 * @param <V>
 */
public class SmeltValueWrapper<V extends SmeltValue<V>> {
    private V smeltValue;

    public SmeltValueWrapper(V smeltValue) {
        super();
        this.smeltValue = smeltValue;
    }

    public V getSmeltValue() {
        return smeltValue;
    }

}
