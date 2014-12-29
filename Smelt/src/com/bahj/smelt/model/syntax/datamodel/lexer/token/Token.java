package com.bahj.smelt.model.syntax.datamodel.lexer.token;

import com.bahj.smelt.model.syntax.datamodel.lexer.Span;

public interface Token {
    /**
     * Retrieves the span of this token.
     * @return The span for this token.
     */
    public Span getSpan();
    
    /**
     * Describes the type of this token.
     * @return A human-readable description of this token's type.
     */
    public String getTypeDescription();
}
