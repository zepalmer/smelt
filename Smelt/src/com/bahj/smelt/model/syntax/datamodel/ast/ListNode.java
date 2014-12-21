package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.List;

public class ListNode implements Ast {
	private List<String> values;

	public ListNode(List<String> values) {
		super();
		this.values = values;
	}

	public List<String> getValues() {
		return values;
	}

}
