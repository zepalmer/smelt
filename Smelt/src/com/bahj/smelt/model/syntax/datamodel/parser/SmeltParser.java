package com.bahj.smelt.model.syntax.datamodel.parser;

import static com.bahj.flapjack.Parsers.apply;
import static com.bahj.flapjack.Parsers.matchesType;
import static com.bahj.flapjack.Parsers.or;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.bahj.flapjack.Context;
import com.bahj.flapjack.ParseException;
import com.bahj.flapjack.Parser;
import com.bahj.flapjack.StandardContext;
import com.bahj.smelt.model.syntax.datamodel.ast.Ast;
import com.bahj.smelt.model.syntax.datamodel.ast.ListNode;
import com.bahj.smelt.model.syntax.datamodel.ast.MessageNode;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.ColonToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.CommaToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.DedentToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.EqualToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.IdentifierToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.IndentToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.Token;

/**
 * A parser for the Smelt file format.
 * 
 * @author Zachary Palmer
 */
public class SmeltParser {
    // @formatter:off
    
	private static final Parser<Token, String> PARSER_IDENTIFIER =
	    matchesType(Token.class, IdentifierToken.class).apply(IdentifierToken::getText);
	
	private static final Parser<Token, List<String>> PARSER_POSITIONAL_ARG =
	    apply(PARSER_IDENTIFIER, Collections::singletonList);

	private static final Parser<Token, Map<String, String>> PARSER_NAMED_ARG =
		apply(
			PARSER_IDENTIFIER
				.andThenIgnoring(matchesType(Token.class, EqualToken.class))
				.pairWith(PARSER_IDENTIFIER),
			(String k, String v) -> Collections.singletonMap(k, v));
	
	private static final MessageNode.Header joinHeaders(Iterable<MessageNode.Header> headers) {
        List<String> name = new ArrayList<>();
        List<String> positional = new ArrayList<>();
        Map<String, String> named = new HashMap<>();
        
        for (MessageNode.Header header : headers) {
            name.addAll(header.getName());
            positional.addAll(header.getPositional());
            named.putAll(header.getNamed());
            // TODO: verify that mappings are unique?
        }
	    return new MessageNode.Header(name, positional, named);
	}

	private static final Parser<Token, MessageNode.Header> PARSER_ARG =
		or(
		    PARSER_NAMED_ARG.apply((Map<String,String> map) ->
		        new MessageNode.Header(Collections.emptyList(), Collections.emptyList(), map)),
		    PARSER_POSITIONAL_ARG.apply((List<String> args) ->
		        new MessageNode.Header(Collections.emptyList(), args, Collections.emptyMap())));		    

	private static final Parser<Token, MessageNode.Header> PARSER_ARGS =
		PARSER_ARG
			.many()
			.apply(SmeltParser::joinHeaders);

	private static final Parser<Token, MessageNode.Header> PARSER_MESSAGE_NODE_HEADER =
		apply(
			PARSER_IDENTIFIER
			    .many()
			    .andThenIgnoring(matchesType(Token.class, ColonToken.class))
			    .pairWith(PARSER_ARGS.maybe()),
			(List<String> name, Optional<MessageNode.Header> args) ->
				{
				    MessageNode.Header header =
				            new MessageNode.Header(name, Collections.emptyList(), Collections.emptyMap());
				    if (args.isPresent()) {
				        return joinHeaders(Arrays.asList(header, args.get()));
					} else {
						return header;
					}
				});

	private static final Parser<Token, List<Ast>> PARSER_MESSAGE_NODE_BODY =
		(Context<Token> ctx) ->
	        PARSER_NODE()
				.many()
				.afterIgnoring(matchesType(Token.class, IndentToken.class))
				.andThenIgnoring(matchesType(Token.class, DedentToken.class))
				.parse(ctx);
	
	private static final Parser<Token, Ast> PARSER_LIST_NODE =
	    apply(
	        PARSER_IDENTIFIER.sepByEnd(matchesType(Token.class, CommaToken.class)),
	        ListNode::new
	        );

	private static final Parser<Token, Ast> PARSER_MESSAGE_NODE =
		apply(
			PARSER_MESSAGE_NODE_HEADER
				.pairWith(PARSER_MESSAGE_NODE_BODY.maybe()),
			(ImmutablePair<MessageNode.Header, Optional<List<Ast>>> pair) ->
				{
				    return new MessageNode(pair.getLeft(), pair.getRight().orElse(Collections.emptyList()));
				});

	private static final Parser<Token, Ast> PARSER_NODE =
	    or(
	        PARSER_MESSAGE_NODE,
	        PARSER_LIST_NODE);

	private static final Parser<Token, Ast> PARSER_NODE() {
	    return PARSER_NODE;
	}
	
	// @formatter:on

    public SmeltParser() {
    }

    public Ast parse(Iterator<Token> tokens) throws ParseException {
        return PARSER_NODE.parse(new StandardContext<>(tokens));
    }
}
