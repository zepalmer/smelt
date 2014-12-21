package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.List;
import java.util.Map;

public class MessageNode implements Ast {
	private String name;
	private List<String> positional;
	private Map<String, String> named;
	private List<Ast> children;

	public MessageNode(String name, List<String> positional,
			Map<String, String> named, List<Ast> children) {
		super();
		this.name = name;
		this.positional = positional;
		this.named = named;
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public List<String> getPositional() {
		return positional;
	}

	public Map<String, String> getNamed() {
		return named;
	}

    public List<Ast> getChildren() {
        return children;
    }
}
