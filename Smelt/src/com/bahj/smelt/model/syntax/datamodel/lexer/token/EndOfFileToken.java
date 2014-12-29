package com.bahj.smelt.model.syntax.datamodel.lexer.token;

import com.bahj.smelt.model.syntax.datamodel.lexer.Span;

public class EndOfFileToken extends AbstractToken {
    public EndOfFileToken(Span span) {
        super(span);
    }

    @Override
    public String getTypeDescription() {
        return "EOF";
    }
}
