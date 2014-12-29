package com.bahj.smelt.model.syntax.datamodel.lexer.token;

import com.bahj.smelt.model.syntax.datamodel.lexer.Span;

public abstract class AbstractToken implements Token {
    private Span span;

    public AbstractToken(Span span) {
        super();
        this.span = span;
    }

    public Span getSpan() {
        return span;
    }
}
