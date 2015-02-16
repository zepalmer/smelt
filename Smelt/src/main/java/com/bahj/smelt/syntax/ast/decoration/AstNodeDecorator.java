package com.bahj.smelt.syntax.ast.decoration;

import java.util.List;

import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.plugin.DeclarationProcessingFailure;
import com.bahj.smelt.syntax.SourceLocation;
import com.bahj.smelt.syntax.ast.AstNode;

public class AstNodeDecorator<T extends AstNode> implements AstNode {
    protected T backingNode;
    protected DecoratorNodeContext context;

    public AstNodeDecorator(T backingNode, DecoratorNodeContext context) {
        super();
        this.backingNode = backingNode;
        this.context = context;
    }

    public DecoratorNodeContext getContext() {
        return context;
    }

    @Override
    public SourceLocation getLocation() {
        return this.backingNode.getLocation();
    }

    @Override
    public String getSimpleDescription() {
        return this.backingNode.getSimpleDescription();
    }

    @Override
    public List<? extends AstNode> getDescriptionChildren() {
        // Description children don't need to be decorated.
        return this.backingNode.getDescriptionChildren();
    }

    /**
     * Generates a standard {@link DeclarationProcessingException} which can be thrown by a decorator node which is
     * doing validity checks. The error location matches this node.
     * 
     * @param message
     *            The message to attach to the exception.
     * @return The exception which can be thrown.
     */
    public DeclarationProcessingException failureWithMessage(String message) {
        return failureWithMessage(message, this.getLocation());
    }

    /**
     * Generates a standard {@link DeclarationProcessingException} which can be thrown by a decorator node which is
     * doing validity checks.
     * 
     * @param message
     *            The message to attach to the exception.
     * @param location
     *            The location reported by the exception.
     * @return The exception which can be thrown.
     */
    public DeclarationProcessingException failureWithMessage(String message, SourceLocation location) {
        return new DeclarationProcessingException(new DeclarationProcessingFailure(this.context.getPlugin(), message,
                location));
    }

}
