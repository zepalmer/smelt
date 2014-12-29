package com.bahj.smelt.model.syntax.datamodel.lexer.token;

import com.bahj.smelt.model.syntax.datamodel.lexer.Span;

public class IdentifierToken extends AbstractToken {
    private String text;
    
    public IdentifierToken(Span span, String text) {
        super(span);
        this.text = text;
    }
    
    @Override
    public String getTypeDescription() {
        return "text";
    }

    public String getText() {
        return text;
    }
}
