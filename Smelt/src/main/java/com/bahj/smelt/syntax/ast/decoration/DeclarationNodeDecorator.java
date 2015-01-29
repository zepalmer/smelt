package com.bahj.smelt.syntax.ast.decoration;

import com.bahj.smelt.syntax.ast.DeclarationNode;

public class DeclarationNodeDecorator<T extends DeclarationNode> extends AstNodeDecorator<T> implements DeclarationNode {
    public DeclarationNodeDecorator(T backingNode, DecoratorNodeContext context) {
        super(backingNode, context);
    }
}
