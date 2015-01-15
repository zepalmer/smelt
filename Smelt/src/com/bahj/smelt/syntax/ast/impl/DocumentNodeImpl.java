package com.bahj.smelt.syntax.ast.impl;

import java.util.List;

import com.bahj.smelt.syntax.SourceLocation;
import com.bahj.smelt.syntax.ast.AstNode;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.DocumentNode;

public class DocumentNodeImpl extends AbstractAstNodeImpl implements DocumentNode {
    private List<DeclarationNode> declarations;

    public DocumentNodeImpl(SourceLocation location, List<DeclarationNode> declarations) {
        super(location);
        this.declarations = declarations;
    }

    @Override
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
