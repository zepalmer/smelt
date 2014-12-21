package com.bahj.flapjack;

import java.util.ListIterator;

/**
 * An exception type used internally to indicate a parse failure.
 * 
 * @author Zachary Palmer
 */
public class ParseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private ListIterator<?> tokens;

	public ParseException(ListIterator<?> tokens) {
		this.tokens = tokens;
	}

	public ListIterator<?> getTokens() {
		return tokens;
	}
}
