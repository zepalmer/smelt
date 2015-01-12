package com.bahj.smelt.syntax.ast;

import java.util.Collections;
import java.util.List;

public class ListNode implements DeclarationNode {
	private List<String> values;

	public ListNode(List<String> values) {
		super();
		this.values = values;
	}

	public List<String> getValues() {
		return values;
	}

    @Override
    public String getSimpleDescription() {
        StringBuilder sb = new StringBuilder();
        for (String value : this.values) {
            if (sb.length() >  0){
                sb.append(", ");
            }
            sb.append(value);
        }
        return sb.toString();
    }

    @Override
    public List<? extends AstNode> getDescriptionChildren() {
        return Collections.emptyList();
    }
}
