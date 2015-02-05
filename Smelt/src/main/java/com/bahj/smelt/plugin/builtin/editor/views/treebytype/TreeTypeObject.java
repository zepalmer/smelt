package com.bahj.smelt.plugin.builtin.editor.views.treebytype;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

public class TreeTypeObject<T extends SmeltType<V>, V extends SmeltValue<V>> implements TreeObject {
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
