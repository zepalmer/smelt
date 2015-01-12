package com.bahj.smelt.syntax.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageHeaderNode implements AstNode {
    private String name;
    private List<PositionalArgumentNode> positional;
    private Map<String, NamedArgumentNode> named;

    public MessageHeaderNode(String name, List<PositionalArgumentNode> positional, Map<String, NamedArgumentNode> named) {
        super();
        this.name = name;
        this.positional = positional;
        this.named = named;
    }

    public String getName() {
        return name;
    }

    public List<PositionalArgumentNode> getPositional() {
        return positional;
    }

    public Map<String, NamedArgumentNode> getNamed() {
        return named;
    }

    @Override
    public String getSimpleDescription() {
        return this.name;
    }

    @Override
    public List<? extends AstNode> getDescriptionChildren() {
        List<ArgumentNode> args = new ArrayList<>();
        args.addAll(positional);
        args.addAll(named.values());
        return args;
    }

}