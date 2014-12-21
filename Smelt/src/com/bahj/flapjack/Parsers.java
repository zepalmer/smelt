package com.bahj.flapjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.bahj.flapjack.function.Function2;
import com.bahj.flapjack.function.Function3;
import com.bahj.flapjack.function.Function4;
import com.bahj.flapjack.function.Function5;

/**
 * Contains a group of predefined parser.
 * 
 * @author Zachary Palmer
 */
public class Parsers {
	/**
	 * Creates a parser which always returns success. This parser consumes no
	 * tokens.
	 * 
	 * @return A successful parser.
	 */
	public static <T> Parser<T, Void> success() {
		return (Context<T> ctx) -> {
			return null;
		};
	}

	/**
	 * Creates a parser which always returns success. This parser consumes no
	 * tokens.
	 * 
	 * @return A successful parser.
	 */
	public static <T, R> Parser<T, R> success(R r) {
		return (Context<T> ctx) -> {
			return r;
		};
	}

	/**
	 * Creates a parser which always returns failure. This parser consumes no
	 * tokens.
	 * 
	 * @return A failed parser.
	 */
	public static <T, R> Parser<T, R> fail() {
		return (Context<T> ctx) -> {
			throw new ParseException(ctx.currentTokens());
		};
	}

	/**
	 * Drops the return type of a parser.
	 * 
	 * @return A parser with no meaningful result.
	 */
	public static <T> Parser<T, Void> silence(Parser<T, ?> parser) {
		return (Context<T> ctx) -> {
			parser.parse(ctx);
			return null;
		};
	}

	/**
	 * Attaches pre-processing actions to a parser. The actions yield no value
	 * but may have side effects.
	 * 
	 * @param parser
	 *            The parser.
	 * @param actions
	 *            The pre-processing actions.
	 * @return The resulting parser.
	 */
	@SafeVarargs
	public static <T, R> Parser<T, R> withPre(Parser<T, R> parser,
			Parser<T, ?>... actions) {
		return (Context<T> ctx) -> {
			for (Parser<T, ?> action : actions) {
				action.parse(ctx);
			}
			return parser.parse(ctx);
		};
	}

	/**
	 * Attaches pre-processing actions to a parser. The actions yield no value
	 * but may have side effects.
	 * 
	 * @param parser
	 *            The parser.
	 * @param actions
	 *            The pre-processing actions.
	 * @return The resulting parser.
	 */
	@SafeVarargs
	public static <T, R> Parser<T, R> withPre(Parser<T, R> parser,
			Consumer<Context<T>>... actions) {
		return (Context<T> ctx) -> {
			for (Consumer<Context<T>> action : actions) {
				action.accept(ctx);
			}
			return parser.parse(ctx);
		};
	}

	/**
	 * Attaches a post-processing action to a parser. The action yields no value
	 * but may have side effects.
	 * 
	 * @param parser
	 *            The parser.
	 * @param action
	 *            The post-processing action.
	 * @return The resulting parser.
	 */
	@SafeVarargs
	public static <T, R> Parser<T, R> withPost(Parser<T, R> parser,
			Parser<T, ?>... actions) {
		return (Context<T> ctx) -> {
			R ret = parser.parse(ctx);
			for (Parser<T, ?> action : actions) {
				action.parse(ctx);
			}
			return ret;
		};
	}

	/**
	 * Attaches a post-processing action to a parser. The action yields no value
	 * but may have side effects.
	 * 
	 * @param parser
	 *            The parser.
	 * @param action
	 *            The post-processing action.
	 * @return The resulting parser.
	 */
	@SafeVarargs
	public static <T, R> Parser<T, R> withPost(Parser<T, R> parser,
			Consumer<Context<T>>... actions) {
		return (Context<T> ctx) -> {
			R ret = parser.parse(ctx);
			for (Consumer<Context<T>> action : actions) {
				action.accept(ctx);
			}
			return ret;
		};
	}

	/**
	 * Creates a parser which matches the next token to a specific predicate.
	 * The token is preserved.
	 * 
	 * @param pred
	 *            The predicate.
	 * @return A parser which produces no value if the predicate matches and a
	 *         failure if it does not.
	 */
	public static <T> Parser<T, T> matches(Function<T, Boolean> pred) {
		return (Context<T> ctx) -> {
			T token = ctx.peek();
			if (pred.apply(token)) {
				return token;
			} else {
				throw new ParseException(ctx.currentTokens());
			}
		};
	}

	/**
	 * Creates a parser which matches the next token to a specific predicate.
	 * The token is discarded.
	 * 
	 * @param pred
	 *            The predicate.
	 * @return A parser which produces no value if the predicate matches and a
	 *         failure if it does not.
	 */
	public static <T> Parser<T, T> consume(Function<T, Boolean> pred) {
		return withPost(matches(pred), (Context<T> ctx) -> ctx.nextToken());
	}

	/**
	 * Creates a parser which applies the first child parser provided. If the
	 * first child failed and consumed tokens, we fail. If the first child
	 * succeeds, it is taken as the action of the parser. If the first child
	 * failed without consuming any tokens, we proceed to the second child and
	 * continue.
	 * 
	 * @param parsers
	 *            The children.
	 * @return The resulting parser.
	 */
	@SafeVarargs
	public static final <T, R> Parser<T, R> or(Parser<T, R>... parsers) {
		return (Context<T> ctx) -> {
			for (Parser<T, R> parser : parsers) {
				ctx.startTransaction();
				try {
					R ret = parser.parse(ctx);
					ctx.commit();
					return ret;
				} catch (ParseException e) {
					final boolean change = ctx.hasChanged();
					ctx.rollback();
					if (change) {
						throw e;
					} else {
						// Try the next parser.
					}
				}
			}
			throw new ParseException(ctx.currentTokens());
		};
	}

	/**
	 * Creates an optional parser which matches a child parser zero or one
	 * times.
	 * 
	 * @param parser
	 *            The parser in question.
	 * @return The resulting parser.
	 */
	public static final <T, R> Parser<T, Optional<R>> maybe(Parser<T, R> parser) {
		return or(apply(parser, (R r) -> Optional.of(r)),
				Parsers.<T, Optional<R>> success(Optional.<R> empty()));
	}

	/**
	 * Creates a parser which matches a child parser zero or more times.
	 * 
	 * @param parser
	 *            The parser in question.
	 * @return The resulting parser.
	 */
	public static final <T, R> Parser<T, List<R>> many(Parser<T, R> parser) {
		return (Context<T> ctx) -> {
			List<R> results = new ArrayList<>();
			boolean stop = false;
			while (!stop) {
				try {
					R r = attempt(parser).parse(ctx);
					results.add(r);
				} catch (ParseException e) {
					stop = true;
				}
			}
			return results;
		};
	}

	/**
	 * Creates a parser which matches a child parser one or more times.
	 * 
	 * @param parser
	 *            The parser in question.
	 * @return The resulting parser.
	 */
	public static final <T, R> Parser<T, List<R>> many1(Parser<T, R> parser) {
		return (Context<T> ctx) -> apply(many(parser), (List<R> list) -> {
			if (list.size() == 0) {
				throw new ParseException(ctx.currentTokens());
			} else {
				return list;
			}
		}).parse(ctx);
	}

	/**
	 * Creates a parser which matches a child parser zero or more times. Each
	 * instance is separated by another parser. An instance of the separator is
	 * permitted to appear at the end as well.
	 * 
	 * @param parser
	 *            The parser in question.
	 * @param sep
	 *            The parser for the separator.
	 * @return The resulting parser.
	 */
	public static final <T, R> Parser<T, List<R>> sepByEnd(Parser<T, R> parser,
			Parser<T, ?> sep) {
		return (Context<T> ctx) -> {
			List<R> results = new ArrayList<>();
			boolean stop = false;
			while (!stop) {
				try {
					R r = attempt(parser).parse(ctx);
					results.add(r);
					try {
						attempt(sep).parse(ctx);
					} catch (ParseException e) {
						stop = true;
					}
				} catch (ParseException e) {
					stop = true;
				}
			}
			return results;
		};
	}

	/**
	 * Creates a parser based on the provided child parser. If the provided
	 * child succeeds, then this parser does in the same way. If the provided
	 * child parser fails, this parser fails in such a way to hide any token
	 * consumption which may have occurred. This is useful to ensure that a
	 * parent disjunction parser (e.g. created by {@#or(Parser...)
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }) will continue even when a multiple token read-ahead is
	 * necessary.
	 * 
	 * @param parser
	 *            The continuation parser.
	 * @return The resulting parser.
	 */
	public static final <T, R> Parser<T, R> attempt(Parser<T, R> parser) {
		return (Context<T> ctx) -> {
			ctx.startTransaction();
			try {
				R ret = parser.parse(ctx);
				ctx.commit();
				return ret;
			} catch (ParseException e) {
				ctx.rollback();
				throw e;
			}
		};
	}

	/**
	 * Executes a list of parsers in sequence and yields a list of their
	 * results.
	 * 
	 * @param parsers
	 *            The parsers to execute.
	 * @return A parser which yields the resulting values.
	 */
	@SafeVarargs
	public static final <T, R> Parser<T, List<R>> sequence(
			Parser<T, R>... parsers) {
		return (Context<T> ctx) -> {
			List<R> results = new ArrayList<R>(parsers.length);
			for (Parser<T, R> parser : parsers) {
				results.add(parser.parse(ctx));
			}
			return results;
		};
	}

	/**
	 * Creates a parser which will apply a function to the result of a parser.
	 * 
	 * @param parser
	 *            The parser to use.
	 * @param function
	 *            The function to apply to that parser's output.
	 * @return The resulting parser.
	 */
	public static <T, P, R> Parser<T, R> apply(Parser<T, P> parser,
			Function<P, R> function) {
		return (Context<T> ctx) -> {
			P p = parser.parse(ctx);
			return function.apply(p);
		};
	}

	/**
	 * Creates a parser which performs an application within the parser
	 * environment.
	 * 
	 * @param parserF
	 *            The parser for the function.
	 * @param parserA
	 *            The parser for the argument.
	 * @return A parser for the resulting type.
	 */
	public static <T, P, R> Parser<T, R> apply(
			Parser<T, Function<P, R>> parserF, Parser<T, P> parserA) {
		return (Context<T> ctx) -> {
			Function<P, R> f = parserF.parse(ctx);
			P p = parserA.parse(ctx);
			return f.apply(p);
		};
	}

	/**
	 * Creates a parser which performs an application within the parser
	 * environment.
	 * 
	 * @param parserF
	 *            The parser for the function.
	 * @param parserA1
	 *            The parser for the first argument.
	 * @param parserA2
	 *            The parser for the second argument.
	 * @return A parser for the resulting type.
	 */
	public static <T, P1, P2, R> Parser<T, R> apply(
			Parser<T, Function2<P1, P2, R>> parserF, Parser<T, P1> parserA1,
			Parser<T, P2> parserA2) {
		return (Context<T> ctx) -> {
			Function2<P1, P2, R> f = parserF.parse(ctx);
			P1 p1 = parserA1.parse(ctx);
			P2 p2 = parserA2.parse(ctx);
			return f.apply(p1, p2);
		};
	}

	/**
	 * Creates a parser which performs an application within the parser
	 * environment.
	 * 
	 * @param parserF
	 *            The parser for the function.
	 * @param parserA1
	 *            The parser for the first argument.
	 * @param parserA2
	 *            The parser for the second argument.
	 * @param parserA3
	 *            The parser for the third argument.
	 * @return A parser for the resulting type.
	 */
	public static <T, P1, P2, P3, R> Parser<T, R> apply(
			Parser<T, Function3<P1, P2, P3, R>> parserF,
			Parser<T, P1> parserA1, Parser<T, P2> parserA2,
			Parser<T, P3> parserA3) {
		return (Context<T> ctx) -> {
			Function3<P1, P2, P3, R> f = parserF.parse(ctx);
			P1 p1 = parserA1.parse(ctx);
			P2 p2 = parserA2.parse(ctx);
			P3 p3 = parserA3.parse(ctx);
			return f.apply(p1, p2, p3);
		};
	}

	/**
	 * Creates a parser which performs an application within the parser
	 * environment.
	 * 
	 * @param parserF
	 *            The parser for the function.
	 * @param parserA1
	 *            The parser for the first argument.
	 * @param parserA2
	 *            The parser for the second argument.
	 * @param parserA3
	 *            The parser for the third argument.
	 * @param parserA4
	 *            The parser for the fourth argument.
	 * @return A parser for the resulting type.
	 */
	public static <T, P1, P2, P3, P4, R> Parser<T, R> apply(
			Parser<T, Function4<P1, P2, P3, P4, R>> parserF,
			Parser<T, P1> parserA1, Parser<T, P2> parserA2,
			Parser<T, P3> parserA3, Parser<T, P4> parserA4) {
		return (Context<T> ctx) -> {
			Function4<P1, P2, P3, P4, R> f = parserF.parse(ctx);
			P1 p1 = parserA1.parse(ctx);
			P2 p2 = parserA2.parse(ctx);
			P3 p3 = parserA3.parse(ctx);
			P4 p4 = parserA4.parse(ctx);
			return f.apply(p1, p2, p3, p4);
		};
	}

	/**
	 * Creates a parser which performs an application within the parser
	 * environment.
	 * 
	 * @param parserF
	 *            The parser for the function.
	 * @param parserA1
	 *            The parser for the first argument.
	 * @param parserA2
	 *            The parser for the second argument.
	 * @param parserA3
	 *            The parser for the third argument.
	 * @param parserA4
	 *            The parser for the fourth argument.
	 * @param parserA5
	 *            The parser for the fifth argument.
	 * @return A parser for the resulting type.
	 */
	public static <T, P1, P2, P3, P4, P5, R> Parser<T, R> apply(
			Parser<T, Function5<P1, P2, P3, P4, P5, R>> parserF,
			Parser<T, P1> parserA1, Parser<T, P2> parserA2,
			Parser<T, P3> parserA3, Parser<T, P4> parserA4,
			Parser<T, P5> parserA5) {
		return (Context<T> ctx) -> {
			Function5<P1, P2, P3, P4, P5, R> f = parserF.parse(ctx);
			P1 p1 = parserA1.parse(ctx);
			P2 p2 = parserA2.parse(ctx);
			P3 p3 = parserA3.parse(ctx);
			P4 p4 = parserA4.parse(ctx);
			P5 p5 = parserA5.parse(ctx);
			return f.apply(p1, p2, p3, p4, p5);
		};
	}
	
	/**
	 * Applies a two-argument function to an uncurried function within the
	 * parser environment.
	 * @param parser The parser for the pair.
	 * @param function The function to use.
	 * @return The resulting parser.
	 */
	public static <T,P1,P2,R> Parser<T,R> apply(Parser<T,ImmutablePair<P1,P2>> parser,
			Function2<P1,P2,R> function) {
		return (Context<T> ctx) -> {
			ImmutablePair<P1,P2> pair = parser.parse(ctx);
			return function.apply(pair.getLeft(), pair.getRight());
		};
	}

	/**
	 * Creates a pair parser.
	 * 
	 * @param parser1
	 *            The first parser.
	 * @param parser2
	 *            The second parser.
	 * @return The resulting parser.
	 */
	public static <T, R1, R2> Parser<T, ImmutablePair<R1, R2>> pair(
			Parser<T, R1> parser1, Parser<T, R2> parser2) {
		return (Context<T> ctx) -> {
			R1 r1 = parser1.parse(ctx);
			R2 r2 = parser2.parse(ctx);
			return new ImmutablePair<R1, R2>(r1, r2);
		};
	}

	/**
	 * Creates parser with a constant pair component.
	 * 
	 * @param parser
	 *            The parser on the left.
	 * @param value
	 *            The value on the right.
	 * @return The resulting parser.
	 */
	public static <T, R1, R2> Parser<T, ImmutablePair<R1, R2>> pair(
			Parser<T, R1> parser, R2 value) {
		return (Context<T> ctx) -> {
			R1 r = parser.parse(ctx);
			return new ImmutablePair<R1, R2>(r, value);
		};
	}

	/**
	 * Creates parser with a constant pair component.
	 * 
	 * @param value
	 *            The value on the left.
	 * @param parser
	 *            The parser on the right.
	 * @return The resulting parser.
	 */
	public static <T, R1, R2> Parser<T, ImmutablePair<R1, R2>> pair(R1 value,
			Parser<T, R2> parser) {
		return (Context<T> ctx) -> {
			R2 r = parser.parse(ctx);
			return new ImmutablePair<R1, R2>(value, r);
		};
	}

	/**
	 * Transforms a void parser into a consumer.
	 * 
	 * @param parser
	 *            The parser to transform.
	 * @return The equivalent consumer.
	 */
	public static <T> Consumer<Context<T>> consumer(Parser<T, ?> parser) {
		return (Context<T> ctx) -> parser.parse(ctx);
	}
}
