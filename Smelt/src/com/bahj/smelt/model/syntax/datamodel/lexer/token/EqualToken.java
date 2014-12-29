package com.bahj.smelt.model.syntax.datamodel.lexer.token;

import com.bahj.smelt.model.syntax.datamodel.lexer.Span;

public class EqualToken extends AbstractToken {
    public EqualToken(Span span) {
        super(span);
    }

    @Override
    public String getTypeDescription() {
        return "'='";
    }
}
