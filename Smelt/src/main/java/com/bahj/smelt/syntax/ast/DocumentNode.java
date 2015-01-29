package com.bahj.smelt.syntax.ast;

import java.util.List;

public interface DocumentNode extends AstNode {
    public List<DeclarationNode> getDeclarations();
}