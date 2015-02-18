package com.bahj.smelt.syntax.ast;

import java.util.List;

public interface DeclarationNode extends AstNode {

    public abstract DeclarationHeaderNode getHeader();

    public abstract List<? extends DeclarationNode> getChildren();

}