package com.bahj.smelt.syntax.ast.decoration;

import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.syntax.ast.DeclarationNode;

public abstract class DeclarationNodeDecorator<T extends DeclarationNode> extends AstNodeDecorator<T> implements
        DeclarationNode {
    public DeclarationNodeDecorator(T backingNode, DecoratorNodeContext context) {
        super(backingNode, context);
    }

    public abstract MessageNodeDecorator insistMessageNode() throws DeclarationProcessingException;

    public abstract ListNodeDecorator insistListNode() throws DeclarationProcessingException;
}
