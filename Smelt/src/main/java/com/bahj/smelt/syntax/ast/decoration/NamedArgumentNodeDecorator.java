package com.bahj.smelt.syntax.ast.decoration;

import java.util.List;

import com.bahj.smelt.syntax.ast.NamedArgumentNode;

public class NamedArgumentNodeDecorator extends AstNodeDecorator<NamedArgumentNode> implements NamedArgumentNode {
    public NamedArgumentNodeDecorator(NamedArgumentNode backingNode, DecoratorNodeContext context) {
        super(backingNode, context);
    }

    @Override
    public String getName() {
        return this.backingNode.getName();
    }

    @Override
    public List<String> getArgs() {
        return this.backingNode.getArgs();
    }
}
