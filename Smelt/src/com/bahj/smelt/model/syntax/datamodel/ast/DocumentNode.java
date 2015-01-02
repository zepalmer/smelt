package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.List;

public class DocumentNode implements AstNode {
    private List<DeclarationNode> declarations;

    public DocumentNode(List<DeclarationNode> declarations) {
        super();
        this.declarations = declarations;
    }

    public List<DeclarationNode> getDeclarations() {
        return declarations;
    }

    @Override
    public String getSimpleDescription() {
        return "<document>";
    }

    @Override
    public List<? extends AstNode> getDescriptionChildren() {
        return this.declarations;
    }

}
