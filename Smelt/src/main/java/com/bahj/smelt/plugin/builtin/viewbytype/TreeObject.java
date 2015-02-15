package com.bahj.smelt.plugin.builtin.viewbytype;

public interface TreeObject {
    public <P, R, X extends Exception, T extends TreeObjectVisitor<P, R, X>> R visit(T visitor, P arg) throws X;
}
