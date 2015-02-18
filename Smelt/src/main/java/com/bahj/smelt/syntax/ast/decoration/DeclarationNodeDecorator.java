package com.bahj.smelt.syntax.ast.decoration;

import java.util.List;
import java.util.stream.Collectors;

import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.syntax.ast.DeclarationNode;

public class DeclarationNodeDecorator extends AstNodeDecorator<DeclarationNode> implements DeclarationNode {
    public DeclarationNodeDecorator(DeclarationNode backingNode, DecoratorNodeContext context) {
        super(backingNode, context);
    }

    @Override
    public DeclarationHeaderNodeDecorator getHeader() {
        return new DeclarationHeaderNodeDecorator(this.backingNode.getHeader(), this.context);
    }

    @Override
    public List<? extends DeclarationNodeDecorator> getChildren() {
        return this.backingNode.getChildren().parallelStream()
                .map((DeclarationNode node) -> new DeclarationNodeDecorator(node, this.context))
                .collect(Collectors.toList());
    }

    /**
     * Insists that this message node has no children. If it does, an appropriate error is raised.
     * 
     * @throws DeclarationProcessingException
     *             If this message node has children.
     */
    public void insistNoChildren() throws DeclarationProcessingException {
        if (this.getChildren().size() > 0) {
            throw failureWithMessage("No children expected here.");
        }
    }

    /**
     * Insists that this message node has a single child. If it has any other number of children, an appropriate error
     * is raised.
     * 
     * @return The child of this node.
     * @throws DeclarationProcessingException
     *             If this node has no children or more than one child.
     */
    public DeclarationNodeDecorator insistOneChild() throws DeclarationProcessingException {
        List<? extends DeclarationNodeDecorator> children = this.getChildren();
        if (children.size() != 1) {
            throw failureWithMessage("This node was expected to have exactly one child.");
        } else {
            return children.get(0);
        }
    }
}
