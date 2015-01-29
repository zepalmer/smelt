package com.bahj.smelt.syntax;

public class SmeltParseFailure {
    private SourceLocation location;
    private String message;

    public SmeltParseFailure(SourceLocation location, String message) {
        super();
        this.location = location;
        this.message = message;
    }

    public SourceLocation getLocation() {
        return location;
    }

    public String getMessage() {
        return message;
    }

}
