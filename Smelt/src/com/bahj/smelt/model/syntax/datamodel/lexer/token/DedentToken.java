package com.bahj.smelt.model.syntax.datamodel.lexer.token;

import com.bahj.smelt.model.syntax.datamodel.lexer.Span;

public class DedentToken extends AbstractToken {
    public DedentToken(Span span) {
        super(span);
    }

    @Override
    public String getTypeDescription() {
        return "dedent";
    }
}
