package com.bahj.smelt.syntax.ast.impl;

import java.util.ArrayList;
import java.util.List;

import com.bahj.smelt.syntax.SourceLocation;
import com.bahj.smelt.syntax.ast.AstNode;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.MessageHeaderNode;
import com.bahj.smelt.syntax.ast.MessageNode;

public class MessageNodeImpl extends AbstractAstNodeImpl implements MessageNode {
    private MessageHeaderNode header;
    private List<DeclarationNode> children;

    public MessageNodeImpl(SourceLocation location, MessageHeaderNode header, List<DeclarationNode> children) {
        super(location);
        this.header = header;
        this.children = children;
    }

    @Override
    public MessageHeaderNode getHeader() {
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
