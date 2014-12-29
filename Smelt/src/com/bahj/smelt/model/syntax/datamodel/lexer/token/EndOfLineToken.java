package com.bahj.smelt.model.syntax.datamodel.lexer.token;

import com.bahj.smelt.model.syntax.datamodel.lexer.Span;

public class EndOfLineToken extends AbstractToken {
    public EndOfLineToken(Span span) {
        super(span);
    }

    @Override
    public String getTypeDescription() {
        return "EOL";
    }
}
