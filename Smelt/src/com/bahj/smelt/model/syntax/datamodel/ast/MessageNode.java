package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.ArrayList;
import java.util.List;

public class MessageNode implements DeclarationNode {
    private MessageHeaderNode header;
    private List<DeclarationNode> children;

    public MessageNode(MessageHeaderNode header, List<DeclarationNode> children) {
        super();
        this.header = header;
        this.children = children;
    }

    public MessageHeaderNode getHeader() {
        return header;
    }

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
