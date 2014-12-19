package com.bahj.smelt.model.syntax.datamodel.parser;

import java.util.Iterator;

import com.bahj.smelt.model.syntax.datamodel.lexer.Token;
import com.nurkiewicz.lazyseq.LazySeq;

/**
 * A parser for the Smelt file format.
 * @author Zachary Palmer
 */
public class SmeltParser {
	private LazySeq<Token> tokens;
	
	public SmeltParser(Iterator<Token> tokens) {
		this.tokens = LazySeq.of(tokens);
	}
	
	// TODO: refactor so that the parser is more concrete -- it should create
	// 	     an appropriate rule from a list of declaration handlers
//	public <T> parse(ParseRule<T> rule) {
//		
//	}
}
