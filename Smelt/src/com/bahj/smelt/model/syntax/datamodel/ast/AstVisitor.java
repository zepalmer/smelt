package com.bahj.smelt.model.syntax.datamodel.ast;

public interface AstVisitor<P,R> {
	public R visitMessageNode(MessageNode node, P p);
	public R visitListNode(ListNode node, P p);
}
