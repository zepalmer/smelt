package com.bahj.smelt.plugin.builtin.data.model.value.utils;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

/**
 * This wrapper object exists solely for the sake of the Java generics system. It allows the self-extending property of
 * the {@link SmeltValue} type parameter to be captured in the object. Thus, in a case where a user wants a
 * {@link SmeltValue} type parameterized specifically by itself but does not know specifically what type of
 * {@link SmeltValue} applies, a {@link SmeltValueWrapper}<code>&lt;?&gt;</code> can be used instead.
 * 
 * @author Zachary Palmer
 * @param <V>
 *            The type of value stored in this wrapper.
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((smeltValue == null) ? 0 : smeltValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SmeltValueWrapper<?> other = (SmeltValueWrapper<?>) obj;
        if (smeltValue == null) {
            if (other.smeltValue != null)
                return false;
        } else if (!smeltValue.equals(other.smeltValue))
            return false;
        return true;
    }
}
