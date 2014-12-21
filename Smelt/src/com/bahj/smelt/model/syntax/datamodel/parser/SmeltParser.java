package com.bahj.smelt.model.syntax.datamodel.parser;

import static com.bahj.flapjack.Parsers.apply;
import static com.bahj.flapjack.Parsers.consume;
import static com.bahj.flapjack.Parsers.or;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.bahj.flapjack.Context;
import com.bahj.flapjack.Parser;
import com.bahj.smelt.model.syntax.datamodel.ast.Ast;
import com.bahj.smelt.model.syntax.datamodel.ast.ListNode;
import com.bahj.smelt.model.syntax.datamodel.ast.MessageNode;
import com.bahj.smelt.model.syntax.datamodel.lexer.Token;

/**
 * A parser for the Smelt file format.
 * 
 * @author Zachary Palmer
 */
public class SmeltParser {
    // @formatter:off
	
	private static final Parser<Token, Token> exactlyToken(Token.Type type) {
		return consume((Token tok) -> tok.getType() == type);
	}
	
	private static final Parser<Token, String> PARSER_TEXT =
		exactlyToken(Token.Type.TEXT)
			.apply((Token tok) -> tok.getText());

	private static final Parser<Token, List<String>> PARSER_POSITIONAL_ARG =
	    apply(
	        PARSER_TEXT,
	        (String arg) -> Collections.singletonList(arg));

	private static final Parser<Token, Map<String, String>> PARSER_NAMED_ARG =
		apply(
			PARSER_TEXT
				.andThenIgnoring(exactlyToken(Token.Type.EQUAL))
				.pairWith(PARSER_TEXT),
			(String k, String v) -> Collections.singletonMap(k, v));

	private static final Parser<Token, ImmutablePair<List<String>, Map<String, String>>> PARSER_ARG =
		or(
			PARSER_NAMED_ARG.pairLeft(Collections.emptyList()),
			PARSER_POSITIONAL_ARG.pairRight(Collections.emptyMap()));

	private static final Parser<Token, ImmutablePair<List<String>, Map<String, String>>> PARSER_ARGS =
		PARSER_ARG
			.many()
			.apply((List<ImmutablePair<List<String>, Map<String, String>>> values) ->
				{
					List<String> list = new ArrayList<>();
					Map<String, String> map = new HashMap<>();
					for (ImmutablePair<List<String>, Map<String, String>> pair : values) {
						list.addAll(pair.getLeft());
						map.putAll(pair.getRight());
					}
					return new ImmutablePair<>(list, map);
				});

	private static final Parser<Token, ImmutablePair<String, ImmutablePair<List<String>, Map<String, String>>>> PARSER_MESSAGE_NODE_HEADER =
		apply(
			PARSER_TEXT.pairWith(PARSER_ARGS.maybe()),
			(String name, Optional<ImmutablePair<List<String>, Map<String, String>>> args) ->
				{
					if (args.isPresent()) {
						return new ImmutablePair<>(name,
								new ImmutablePair<>(args.get().getLeft(),
										args.get().getRight()));
					} else {
						return new ImmutablePair<>(name,
								new ImmutablePair<>(
										Collections.emptyList(),
										Collections.emptyMap()));
					}
				});

	private static final Parser<Token, List<Ast>> PARSER_MESSAGE_NODE_BODY =
		(Context<Token> ctx) ->
	        PARSER_NODE()
				.many()
				.afterIgnoring(exactlyToken(Token.Type.INDENT))
				.andThenIgnoring(exactlyToken(Token.Type.DEDENT))
				.parse(ctx);
	
	private static final Parser<Token, Ast> PARSER_LIST_NODE =
	    apply(
	        PARSER_TEXT.sepByEnd(exactlyToken(Token.Type.COMMA)),
	        (List<String> terms) -> new ListNode(terms)
	        );

	private static final Parser<Token, Ast> PARSER_MESSAGE_NODE =
		apply(
			PARSER_MESSAGE_NODE_HEADER
				.pairWith(PARSER_MESSAGE_NODE_BODY.maybe()),
			(ImmutablePair<ImmutablePair<String, ImmutablePair<List<String>, Map<String, String>>>, Optional<List<Ast>>> pair) ->
				{
					String name = pair.getLeft().getLeft();
					List<String> posArgs = pair.getLeft().getRight().getLeft();
					Map<String,String> namArgs = pair.getLeft().getRight().getRight();
					List<Ast> children = pair.getRight().orElse(Collections.emptyList());
					return new MessageNode(name, posArgs, namArgs, children);
				});

	private static final Parser<Token, Ast> PARSER_NODE =
	    or(
	        PARSER_MESSAGE_NODE,
	        PARSER_LIST_NODE);

	private static final Parser<Token, Ast> PARSER_NODE() {
	    return PARSER_NODE;
	}
	
	// @formatter:on

    public SmeltParser(Iterator<Token> tokens) {
        // TODO
    }
}
