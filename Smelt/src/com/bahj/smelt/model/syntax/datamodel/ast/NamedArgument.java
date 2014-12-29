package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.List;

public class NamedArgument {
    private String name;
    private List<String> args;

    public NamedArgument(String name, List<String> args) {
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

}
