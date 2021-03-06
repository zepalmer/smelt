package com.bahj.smelt.syntax;

import java.util.List;

public class SmeltParseFailureException extends Exception {
    private static final long serialVersionUID = 1L;

    private List<SmeltParseFailure> failures;

    public SmeltParseFailureException(List<SmeltParseFailure> failures) {
        super(messageFromFailures(failures));
        this.failures = failures;
    }

    public List<SmeltParseFailure> getFailures() {
        return failures;
    }

    private static String messageFromFailures(List<SmeltParseFailure> failures) {
        StringBuilder sb = new StringBuilder();
        for (SmeltParseFailure failure : failures) {
            sb.append(failure.getLocation().getLine()).append(",").append(failure.getLocation().getColumn())
                    .append(": ").append(failure.getMessage()).append('\n');
        }
        return sb.toString();
    }
}
