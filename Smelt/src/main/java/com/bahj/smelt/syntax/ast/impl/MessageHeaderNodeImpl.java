package com.bahj.smelt.syntax.ast.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bahj.smelt.syntax.SourceLocation;
import com.bahj.smelt.syntax.ast.ArgumentNode;
import com.bahj.smelt.syntax.ast.AstNode;
import com.bahj.smelt.syntax.ast.MessageHeaderNode;
import com.bahj.smelt.syntax.ast.NamedArgumentNode;
import com.bahj.smelt.syntax.ast.PositionalArgumentNode;

public class MessageHeaderNodeImpl extends AbstractAstNodeImpl implements MessageHeaderNode {
    private String name;
    private List<? extends PositionalArgumentNode> positional;
    private Map<String, ? extends NamedArgumentNode> named;

    public MessageHeaderNodeImpl(SourceLocation location, String name,
            List<? extends PositionalArgumentNode> positional, Map<String, ? extends NamedArgumentNode> named) {
        super(location);
        this.name = name;
        this.positional = positional;
        this.named = named;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<? extends PositionalArgumentNode> getPositional() {
        return positional;
    }

    @Override
    public Map<String, ? extends NamedArgumentNode> getNamed() {
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