package com.bahj.smelt.model.syntax.datamodel.lexer;

/**
 * Raised when an error occurs while lexing a Smelt data model file.
 * 
 * @author Zachary Palmer
 */
public class LexerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private SourceLocation location;
	private String errorMessage;

	public LexerException(SourceLocation location, String errorMessage) {
		super(location + ": " + errorMessage);
		this.location = location;
		this.errorMessage = errorMessage;
	}
	
	public LexerException(SourceLocation location, Throwable cause) {
		super(cause);
		this.location = location;
	}

	public SourceLocation getLocation() {
		return location;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
