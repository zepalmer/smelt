package com.bahj.smelt.model.syntax.datamodel.lexer.token;

import com.bahj.smelt.model.syntax.datamodel.lexer.Span;

public class CommaToken extends AbstractToken {

    public CommaToken(Span span) {
        super(span);
    }

    @Override
    public String getTypeDescription() {
        return "','";
    }
}
