package com.bahj.smelt.model.syntax.datamodel.parser;

import java.util.List;

import com.bahj.smelt.model.syntax.datamodel.lexer.Token;
import com.bahj.smelt.model.syntax.datamodel.lexer.Token.Type;
import com.nurkiewicz.lazyseq.LazySeq;

public class ExpectedTokensParserException extends ParserException {
	private static final long serialVersionUID = 1L;
	
	private List<Token.Type> expected;

	public ExpectedTokensParserException(LazySeq<Token> tokens,
			List<Type> expected) {
		super(tokens, messageFrom(tokens, expected));
		this.expected = expected;
	}

	public List<Token.Type> getExpected() {
		return expected;
	}

	private static String messageFrom(LazySeq<Token> tokens, List<Type> expected) {
		Token token = tokens.headOption().orElse(null);
		String tokenDescription = token == null ? Token.Type.EOF_DESCRIPTION
				: token.getType().getDescription();
		StringBuilder sb = new StringBuilder();
		sb.append("Unexpected token ");
		sb.append(tokenDescription);
		sb.append("; expected one of:");
		for (Token.Type type : expected) {
			sb.append(type.getDescription());
		}
		return sb.toString();
	}
}
