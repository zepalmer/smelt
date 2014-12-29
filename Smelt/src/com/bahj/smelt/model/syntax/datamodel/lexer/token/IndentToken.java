package com.bahj.smelt.model.syntax.datamodel.lexer.token;

import com.bahj.smelt.model.syntax.datamodel.lexer.Span;

public class IndentToken extends AbstractToken {
    public IndentToken(Span span) {
        super(span);
    }

    @Override
    public String getTypeDescription() {
        return "indent";
    }
}
