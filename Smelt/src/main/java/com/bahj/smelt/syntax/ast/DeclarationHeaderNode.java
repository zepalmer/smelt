package com.bahj.smelt.syntax.ast;

import java.util.List;
import java.util.Map;

public interface DeclarationHeaderNode extends AstNode {

    public String getName();

    public List<? extends PositionalArgumentNode> getPositional();

    public Map<String, ? extends NamedArgumentNode> getNamed();

}