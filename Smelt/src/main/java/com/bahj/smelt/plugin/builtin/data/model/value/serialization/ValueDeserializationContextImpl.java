package com.bahj.smelt.plugin.builtin.data.model.value.serialization;

import java.util.Map;

public class ValueDeserializationContextImpl implements ValueDeserializationContext {
    private Map<Integer,String> classNameMap;

    public ValueDeserializationContextImpl(Map<Integer,String> classNameMap) {
        this.classNameMap = classNameMap;
    }

    @Override
    public String getClassNameByID(int id) {
        return this.classNameMap.get(id);
    }
}
