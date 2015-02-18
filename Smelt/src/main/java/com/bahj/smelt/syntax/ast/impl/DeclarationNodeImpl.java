package com.bahj.smelt.syntax.ast.impl;

import java.util.ArrayList;
import java.util.List;

import com.bahj.smelt.syntax.SourceLocation;
import com.bahj.smelt.syntax.ast.AstNode;
import com.bahj.smelt.syntax.ast.DeclarationHeaderNode;
import com.bahj.smelt.syntax.ast.DeclarationNode;

public class DeclarationNodeImpl extends AbstractAstNodeImpl implements DeclarationNode {
    private DeclarationHeaderNode header;
    private List<DeclarationNode> children;

    public DeclarationNodeImpl(SourceLocation location, DeclarationHeaderNode header, List<DeclarationNode> children) {
        super(location);
        this.header = header;
        this.children = children;
    }

    @Override
    public DeclarationHeaderNode getHeader() {
        return header;
    }

    @Override
    public List<DeclarationNode> getChildren() {
        return children;
    }

    @Override
    public List<? extends AstNode> getDescriptionChildren() {
        List<AstNode> descChildren = new ArrayList<>();
        descChildren.add(header);
        descChildren.addAll(this.children);
        return descChildren;
    }

    @Override
    public String getSimpleDescription() {
        return "<message>";
    }
}
