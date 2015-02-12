package com.bahj.smelt.plugin.builtin.data.model.value.serialization;

import java.util.HashMap;
import java.util.Map;

public class ValueSerializationContextImpl implements ValueSerializationContext {
    private int nextID;
    private Map<String, Integer> mappedNames;
    
    public ValueSerializationContextImpl() {
        this.mappedNames = new HashMap<>();
    }

    @Override
    public int getClassNameID(String className) {
        Integer result = this.mappedNames.get(className);
        if (result == null) {
            result = this.nextID++;
            this.mappedNames.put(className, result);
        }
        return result;
    }
    
    public Map<String, Integer> getMappedNames() {
        return this.mappedNames;
    }
}
