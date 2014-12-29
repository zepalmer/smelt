package com.bahj.smelt.model.syntax.datamodel.lexer;

public class Span {
    private SourceLocation start;
    private SourceLocation end;

    public Span(SourceLocation start, SourceLocation end) {
        super();
        this.start = start;
        this.end = end;
    }

    public SourceLocation getStart() {
        return start;
    }

    public SourceLocation getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return start + " -- " + end;
    }
}
