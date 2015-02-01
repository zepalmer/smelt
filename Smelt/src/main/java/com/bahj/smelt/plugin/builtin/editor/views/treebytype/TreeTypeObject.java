package com.bahj.smelt.plugin.builtin.editor.views.treebytype;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;

public class TreeTypeObject implements TreeObject {
    private SmeltType<?> type;

    public TreeTypeObject(SmeltType<?> type) {
        super();
        this.type = type;
    }

    public SmeltType<?> getType() {
        return type;
    }

    @Override
    public <P, R, X extends Exception, T extends TreeObjectVisitor<P, R, X>> R visit(T visitor, P arg) throws X {
        return visitor.visitType(this, arg);
    }
    
    @Override
    public String toString() {
        return this.type.getName();
    }
}
