package com.bahj.smelt.model.syntax.datamodel.parser;

import java.text.ParseException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.function.Function;

import com.bahj.smelt.model.syntax.datamodel.lexer.Token;
import com.nurkiewicz.lazyseq.LazySeq;

/**
 * An object representing the context in which the Smelt parser operates.
 * 
 * @author Zachary Palmer
 */
public class ParseContext {
	private LazySeq<Token> tokens;

	public ParseContext(LazySeq<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Retrieves the token at the head of the token stream.
	 * 
	 * @return The next token in the token stream.
	 * @throws NoSuchElementException
	 *             If no such token exists.
	 */
	public Token peek() {
		return this.tokens.head();
	}

	/**
	 * Retrieves the next token in the token stream, consuming it.
	 * 
	 * @return The next token.
	 * @throws NoSuchElementException
	 *             If no such token exists.
	 */
	public Token take() {
		Token token = this.tokens.head();
		this.tokens = this.tokens.tail();
		return token;
	}

	/**
	 * Consumes a token but insists that it matches the provided predicate
	 * function.
	 * 
	 * @param f
	 *            The predicate function.
	 * @param expectation
	 *            A message describing what was expected.
	 * @throws ParseException
	 *             If a parsing error occurred.
	 */
	public void consume(Function<Token, Boolean> predicate, String expectation) {
		Token token = peek();
		if (!predicate.apply(token)) {
			throw new ParserException(this.tokens, "Unexpected token " + token
					+ ".  " + expectation);
		} else {
			take();
		}
	}

	/**
	 * Consumes a token, insisting that it is of a given type.
	 * 
	 * @param type
	 *            The type of the token.
	 * @throws ParseException
	 *             If a parsing error occurred.
	 */
	public void consume(Token.Type type) {
		Token token = peek();
		if (!token.getType().equals(type)) {
			throw new ExpectedTokensParserException(this.tokens,
					Collections.singletonList(type));
		} else {
			take();
		}
	}
}
