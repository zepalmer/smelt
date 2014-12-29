package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.List;
import java.util.Map;

public class MessageNodeHeader {
    private String name;
    private List<PositionalArgument> positional;
    private Map<String, NamedArgument> named;

    public MessageNodeHeader(String name, List<PositionalArgument> positional, Map<String, NamedArgument> named) {
        super();
        this.name = name;
        this.positional = positional;
        this.named = named;
    }

    public String getName() {
        return name;
    }

    public List<PositionalArgument> getPositional() {
        return positional;
    }

    public Map<String, NamedArgument> getNamed() {
        return named;
    }

}