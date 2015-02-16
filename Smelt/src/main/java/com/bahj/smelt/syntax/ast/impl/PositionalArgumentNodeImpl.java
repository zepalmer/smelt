package com.bahj.smelt.syntax.ast.impl;

import java.util.Collections;
import java.util.List;

import com.bahj.smelt.syntax.SourceLocation;
import com.bahj.smelt.syntax.ast.ArgumentNode;
import com.bahj.smelt.syntax.ast.AstNode;
import com.bahj.smelt.syntax.ast.PositionalArgumentNode;

public class PositionalArgumentNodeImpl extends AbstractAstNodeImpl implements ArgumentNode, PositionalArgumentNode {
    private List<String> components;

    public PositionalArgumentNodeImpl(SourceLocation location, List<String> components) {
        super(location);
        this.components = components;
    }

    @Override
    public List<String> getComponents() {
        return components;
    }

    @Override
    public String getSimpleDescription() {
        StringBuilder sb = new StringBuilder();
        for (String component : this.components) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(component);
        }
        return sb.toString();
    }

    @Override
    public List<? extends AstNode> getDescriptionChildren() {
        return Collections.emptyList();
    }
}
