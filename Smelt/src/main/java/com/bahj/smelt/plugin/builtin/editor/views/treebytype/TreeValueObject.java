package com.bahj.smelt.plugin.builtin.editor.views.treebytype;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;

public class TreeValueObject implements TreeObject {
    private SmeltValue<?> value;

    public TreeValueObject(SmeltValue<?> value) {
        super();
        this.value = value;
    }

    public SmeltValue<?> getValue() {
        return value;
    }

    @Override
    public <P, R, X extends Exception, T extends TreeObjectVisitor<P, R, X>> R visit(T visitor, P arg) throws X {
        return visitor.visitValue(this, arg);
    }
    
    @Override
    public String toString() {
        // TODO: something smarter than this
        return super.toString();
    }
}
