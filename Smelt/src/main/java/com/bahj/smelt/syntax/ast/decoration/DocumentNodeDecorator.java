package com.bahj.smelt.syntax.ast.decoration;

import java.util.List;
import java.util.stream.Collectors;

import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.DocumentNode;

public class DocumentNodeDecorator extends AstNodeDecorator<DocumentNode> implements DocumentNode {
    public DocumentNodeDecorator(DocumentNode backingNode, DecoratorNodeContext context) {
        super(backingNode, context);
    }

    @Override
    public List<? extends DeclarationNodeDecorator> getDeclarations() {
        return this.backingNode.getDeclarations().parallelStream()
                .map((DeclarationNode node) -> new DeclarationNodeDecorator(node, this.getContext()))
                .collect(Collectors.toList());
    }
}
