package com.bahj.smelt.syntax.ast.decoration;

import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.ListNode;
import com.bahj.smelt.syntax.ast.MessageNode;

public class DecorationUtils {
    public static DeclarationNodeDecorator<?> decorateDeclarationNode(DeclarationNode node, DecoratorNodeContext context) {
        if (node instanceof MessageNode) {
            return new MessageNodeDecorator((MessageNode) node, context);
        } else if (node instanceof ListNode) {
            return new ListNodeDecorator((ListNode) node, context);
        } else {
            throw new IllegalStateException("Unrecognized declaration node type: " + node.getClass());
        }
    }
}
