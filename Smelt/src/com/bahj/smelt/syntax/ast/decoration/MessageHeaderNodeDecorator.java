package com.bahj.smelt.syntax.ast.decoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bahj.smelt.plugin.DeclarationProcessingException;
import com.bahj.smelt.syntax.ast.MessageHeaderNode;
import com.bahj.smelt.syntax.ast.NamedArgumentNode;
import com.bahj.smelt.syntax.ast.PositionalArgumentNode;

public class MessageHeaderNodeDecorator extends AstNodeDecorator<MessageHeaderNode> implements MessageHeaderNode {
    public MessageHeaderNodeDecorator(MessageHeaderNode backingNode, DecoratorNodeContext context) {
        super(backingNode, context);
    }

    @Override
    public String getName() {
        return this.backingNode.getName();
    }

    @Override
    public List<? extends PositionalArgumentNodeDecorator> getPositional() {
        return this.backingNode.getPositional().parallelStream()
                .map((PositionalArgumentNode node) -> new PositionalArgumentNodeDecorator(node, this.context))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, ? extends NamedArgumentNodeDecorator> getNamed() {
        Map<String, NamedArgumentNodeDecorator> ret = new HashMap<>();
        for (Map.Entry<String, ? extends NamedArgumentNode> entry : this.backingNode.getNamed().entrySet()) {
            ret.put(entry.getKey(), new NamedArgumentNodeDecorator(entry.getValue(), this.context));
        }
        return ret;
    }

    /**
     * Insists that there are no named arguments on this header node. If there are, an appropriate exception is raised.
     * 
     * @throws DeclarationProcessingException
     *             If there are any named arguments on this header node.
     */
    public void insistNoNamedArguments() throws DeclarationProcessingException {
        if (this.getNamed().size() > 0) {
            throw failureWithMessage("Named arguments not expected here.");
        }
    }

    /**
     * Insists that there are no positional arguments on this header node. If there are, an appropriate exception is
     * raised.
     * 
     * @throws DeclarationProcessingException
     *             If there are any positional arguments on this header node.
     */
    public void insistNoPositionalArguments() throws DeclarationProcessingException {
        if (this.getPositional().size() > 0) {
            throw failureWithMessage("Positional arguments not expected here.");
        }
    }

    /**
     * Insists that there is exactly one positional argument on this header node. If there is any other number of
     * positional arguments, an appropriate exception is raised.
     * 
     * @param description
     *            A simple description of the expected positional argument (e.g. "name" or "type").
     * @throws DeclarationProcessingException
     *             If the number of positional arguments on this node is not exactly one.
     * @return The single positional argument node, if it exists.
     */
    public PositionalArgumentNodeDecorator insistSinglePositionalArgument(String description)
            throws DeclarationProcessingException {
        if (this.getPositional().size() == 0) {
            throw failureWithMessage("Missing positional argument: " + description + " expected here.");
        } else if (this.getPositional().size() > 1) {
            throw failureWithMessage("Too many positional arguments.");
        } else {
            return this.getPositional().get(0);
        }
    }
}
