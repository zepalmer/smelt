package com.bahj.smelt.plugin.builtin.editor.views.treebytype;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;

public class TreeValueObject<V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> implements TreeObject {
    private V value;

    public TreeValueObject(V value) {
        super();
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    @Override
    public <P, R, X extends Exception, T extends TreeObjectVisitor<P, R, X>> R visit(T visitor, P arg) throws X {
        return visitor.visitValue(this, arg);
    }

    @Override
    public String toString() {
        return this.value.getDescription();
    }
}
