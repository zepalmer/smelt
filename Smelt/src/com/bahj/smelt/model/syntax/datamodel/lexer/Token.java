package com.bahj.smelt.model.syntax.datamodel.lexer;

/**
 * A token for the Smelt file format.
 * 
 * @author Zachary Palmer
 */
public class Token {
	/**
	 * The types of tokens in the data model file format.
	 * @author Zachary Palmer
	 */
	public static enum Type {
		TEXT("text"),
		COMMA(","),
		COLON(":"),
		EQUAL("="),
		INDENT("indent"),
		DEDENT("dedent"),
		EOL("end-of-line"),
		EOF("end-of-file");
		
		public static final String EOF_DESCRIPTION = "<eof>";
		
		private String description;

		private Type(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}
	
	private SourceLocation location;
	private String text;
	private Type type;

	public Token(SourceLocation location, String text, Type type) {
		super();
		this.location = location;
		this.text = text;
		this.type = type;
	}

	public SourceLocation getLocation() {
		return location;
	}

	public String getText() {
		return text;
	}

	public Type getType() {
		return type;
	}
	
	public String toString() {
		return "Token @"+this.location+" (" + this.type + "): \"" + this.text + "\"";
	}
}
