package com.bahj.smelt.plugin.builtin.data.model.value.serialization;

import com.bahj.smelt.plugin.builtin.data.model.value.utils.SmeltValueWrapper;
import com.bahj.smelt.serialization.json.SmeltJSONSerializationStrategy;

/**
 * An interface implemented by the serialization strategies used for smelt values.
 * 
 * @author Zachary Palmer
 *
 * @param <V>
 *            The type of Smelt value serialized by this strategy.
 */
public interface SmeltValueSerializationStrategy extends SmeltJSONSerializationStrategy<SmeltValueWrapper<?, ?>> {
}
