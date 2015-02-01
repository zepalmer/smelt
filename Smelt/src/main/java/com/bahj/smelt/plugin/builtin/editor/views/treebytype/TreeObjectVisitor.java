package com.bahj.smelt.plugin.builtin.editor.views.treebytype;

public interface TreeObjectVisitor<P, R, X extends Exception> {
    public R visitType(TreeTypeObject obj, P arg) throws X;
    public R visitValue(TreeValueObject obj, P arg) throws X;
}
