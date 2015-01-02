package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.Collections;
import java.util.List;

public class NamedArgumentNode implements ArgumentNode {
    private String name;
    private List<String> args;

    public NamedArgumentNode(String name, List<String> args) {
        super();
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public String getSimpleDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append(" =");
        for (String arg : this.args) {
            sb.append(" ").append(arg);
        }
        return sb.toString();
    }

    @Override
    public List<? extends AstNode> getDescriptionChildren() {
        return Collections.emptyList();
    }
}
