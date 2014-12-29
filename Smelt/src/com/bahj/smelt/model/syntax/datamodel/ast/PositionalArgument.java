package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.List;

public class PositionalArgument {
    private List<String> components;

    public PositionalArgument(List<String> components) {
        super();
        this.components = components;
    }

    public List<String> getComponents() {
        return components;
    }
    
}
