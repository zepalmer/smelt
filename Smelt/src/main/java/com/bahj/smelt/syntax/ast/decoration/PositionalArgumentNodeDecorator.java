package com.bahj.smelt.syntax.ast.decoration;

import java.util.List;

import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.syntax.ast.PositionalArgumentNode;

public class PositionalArgumentNodeDecorator extends AstNodeDecorator<PositionalArgumentNode> implements
        PositionalArgumentNode {
    public PositionalArgumentNodeDecorator(PositionalArgumentNode backingNode, DecoratorNodeContext context) {
        super(backingNode, context);
    }

    @Override
    public List<String> getComponents() {
        return this.backingNode.getComponents();
    }

    /**
     * Insists that this positional argument consists of only one component.
     * 
     * @throws DeclarationProcessingException
     *             If this node contains no components or more than one component.
     * @return The component, if there is only one of them.
     */
    public String insistSingleComponent() throws DeclarationProcessingException {
        if (this.getComponents().size() == 0) {
            throw failureWithMessage("Positional argument expected to contain at least one component.");
        } else if (this.getComponents().size() > 1) {
            throw failureWithMessage("Multiple components not expected here.");
        } else {
            return this.getComponents().get(0);
        }
    }
}
