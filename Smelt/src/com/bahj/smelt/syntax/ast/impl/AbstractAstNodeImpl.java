package com.bahj.smelt.syntax.ast.impl;

import com.bahj.smelt.syntax.SourceLocation;
import com.bahj.smelt.syntax.ast.AstNode;

public abstract class AbstractAstNodeImpl implements AstNode {
    private SourceLocation location;

    public AbstractAstNodeImpl(SourceLocation location) {
        super();
        this.location = location;
    }

    public SourceLocation getLocation() {
        return location;
    }
}
