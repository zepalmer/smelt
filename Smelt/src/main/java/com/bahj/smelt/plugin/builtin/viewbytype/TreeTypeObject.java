package com.bahj.smelt.plugin.builtin.viewbytype;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;

public class TreeTypeObject<T extends SmeltType<V, E>, V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>>
        implements TreeObject {
    private T type;

    public TreeTypeObject(T type) {
        super();
        this.type = type;
    }

    public T getType() {
        return type;
    }

    @Override
    public <P, R, X extends Exception, Z extends TreeObjectVisitor<P, R, X>> R visit(Z visitor, P arg) throws X {
        return visitor.visitType(this, arg);
    }

    @Override
    public String toString() {
        return this.type.getName();
    }
}
