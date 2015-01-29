package com.bahj.smelt.syntax.ast.impl;

import java.util.Collections;
import java.util.List;

import com.bahj.smelt.syntax.SourceLocation;
import com.bahj.smelt.syntax.ast.AstNode;
import com.bahj.smelt.syntax.ast.NamedArgumentNode;

public class NamedArgumentNodeImpl extends AbstractAstNodeImpl implements NamedArgumentNode {
    private String name;
    private List<String> args;

    public NamedArgumentNodeImpl(SourceLocation location, String name, List<String> args) {
        super(location);
        this.name = name;
        this.args = args;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
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
