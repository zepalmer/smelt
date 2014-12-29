package com.bahj.smelt.model.syntax.datamodel.lexer.token;

import com.bahj.smelt.model.syntax.datamodel.lexer.Span;

public class ColonToken extends AbstractToken {
    public ColonToken(Span span) {
        super(span);
    }

    @Override
    public String getTypeDescription() {
        return "','";
    }
}
