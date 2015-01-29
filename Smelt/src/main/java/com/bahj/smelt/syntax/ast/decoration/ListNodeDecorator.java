package com.bahj.smelt.syntax.ast.decoration;

import java.util.List;

import com.bahj.smelt.syntax.ast.ListNode;

public class ListNodeDecorator extends DeclarationNodeDecorator<ListNode> implements ListNode {
    public ListNodeDecorator(ListNode backingNode, DecoratorNodeContext context) {
        super(backingNode, context);
    }

    @Override
    public List<String> getValues() {
        return this.backingNode.getValues();
    }
}
