package com.bahj.smelt.model.syntax.datamodel.parser;

import com.bahj.smelt.model.syntax.datamodel.lexer.Token;
import com.nurkiewicz.lazyseq.LazySeq;

/**
 * An exception type raised when an error occurs parsing a Smelt data model.
 * 
 * @author Zachary Palmer
 */
public class ParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private LazySeq<Token> tokens;
	private String errorMessage;

	public ParserException(LazySeq<Token> tokens, String errorMessage) {
		super(messageFrom(tokens, errorMessage));
		this.tokens = tokens;
		this.errorMessage = errorMessage;
	}

	public LazySeq<Token> getTokens() {
		return tokens;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	private static final String messageFrom(LazySeq<Token> tokens,
			String errorMessage) {
		Token token = tokens.headOption().orElse(null);
		return (token == null ? "<eof>" : token.getLocation().toString())
				+ ": " + errorMessage;
	}
}
