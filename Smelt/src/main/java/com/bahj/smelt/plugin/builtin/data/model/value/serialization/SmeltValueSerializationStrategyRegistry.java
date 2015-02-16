package com.bahj.smelt.plugin.builtin.data.model.value.serialization;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.bahj.smelt.plugin.builtin.data.model.value.utils.SmeltValueWrapper;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.google.gson.JsonElement;

/**
 * A structure containing the {@link SmeltValueSerializationStrategy} objects which are used to serialize and
 * deserialize Smelt values.
 * 
 * @author Zachary Palmer
 *
 */
public class SmeltValueSerializationStrategyRegistry {
    // NOTE: At the moment, this class is very simple. It may be modified in the future to index the strategies.
    private Set<SmeltValueSerializationStrategy> strategies;

    public SmeltValueSerializationStrategyRegistry() {
        super();
        this.strategies = new HashSet<>();
    }

    public void registerSerializationStrategy(SmeltValueSerializationStrategy strategy) {
        this.strategies.add(strategy);
    }

    public Set<SmeltValueSerializationStrategy> getStrategies() {
        return Collections.unmodifiableSet(strategies);
    }

    private void checkNonEmpty() {
        if (this.strategies.size() == 0) {
            throw new IllegalStateException("Cannot serialize a database with zero value strategies.");
        }
    }

    public JsonElement serializeValue(SmeltValueWrapper<?, ?> valueWrapper) throws SerializationException {
        checkNonEmpty();
        SerializationException exception = null;
        for (SmeltValueSerializationStrategy strategy : this.strategies) {
            try {
                return strategy.objectToJson(valueWrapper);
            } catch (SerializationException e) {
                exception = e;
            }
        }
        throw exception;
    }

    public SmeltValueWrapper<?, ?> deserializeValue(JsonElement json) throws DeserializationException {
        checkNonEmpty();
        DeserializationException exception = null;
        for (SmeltValueSerializationStrategy strategy : this.strategies) {
            try {
                return strategy.jsonToObject(json);
            } catch (DeserializationException e) {
                exception = e;
            }
        }
        throw exception;
    }

}
