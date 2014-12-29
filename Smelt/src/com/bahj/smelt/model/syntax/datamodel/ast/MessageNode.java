package com.bahj.smelt.model.syntax.datamodel.ast;

import java.util.List;

public class MessageNode implements Ast {
    private MessageNodeHeader header;
    private List<Ast> children;

    public MessageNode(MessageNodeHeader header, List<Ast> children) {
        super();
        this.header = header;
        this.children = children;
    }

    public MessageNodeHeader getHeader() {
        return header;
    }

    public List<Ast> getChildren() {
        return children;
    }
}
