package com.bahj.smelt.syntax.ast.decoration;

import java.util.List;

import com.bahj.smelt.plugin.DeclarationProcessingException;
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
    
    public String insistOneArgument() throws DeclarationProcessingException {
        if (this.getArgs().size() != 1) {
            throw failureWithMessage("Expected exactly one argument.");
        } else {
            return this.getArgs().get(0);
        }
    }
}
