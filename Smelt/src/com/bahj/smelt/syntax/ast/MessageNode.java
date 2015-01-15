package com.bahj.smelt.syntax.ast;

import java.util.List;

public interface MessageNode extends DeclarationNode {

    public abstract MessageHeaderNode getHeader();

    public abstract List<? extends DeclarationNode> getChildren();

}