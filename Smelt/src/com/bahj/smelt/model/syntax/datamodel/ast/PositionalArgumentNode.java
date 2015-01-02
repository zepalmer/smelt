package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.Collections;
import java.util.List;

public class PositionalArgumentNode implements ArgumentNode {
    private List<String> components;

    public PositionalArgumentNode(List<String> components) {
        super();
        this.components = components;
    }

    public List<String> getComponents() {
        return components;
    }

    @Override
    public String getSimpleDescription() {
        StringBuilder sb = new StringBuilder();
        for (String component : this.components) {
            if (sb.length()>0) {
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
