package com.bahj.smelt.syntax;

public class SmeltParseFailure {
    private int lineStart;
    private int columnStart;
    private String message;

    public SmeltParseFailure(int lineStart, int columnStart, String message) {
        super();
        this.lineStart = lineStart;
        this.columnStart = columnStart;
        this.message = message;
    }

    public int getLineStart() {
        return lineStart;
    }

    public int getColumnStart() {
        return columnStart;
    }

    public String getMessage() {
        return message;
    }

}
