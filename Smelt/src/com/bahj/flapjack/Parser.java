package com.bahj.flapjack;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Defines a parser action.
 * @author Zachary Palmer
 *
 * @param <T> The type of token for this action.
 * @param <R> The return type for this action.
 */
public interface Parser<T,R> {
	default public R parse(Iterator<T> tokens) {
		return this.parse(new StandardContext<T>(tokens));
	}
	
	public R parse(Context<T> context);
	
	default public Parser<T,R> andThenIgnoring(Parser<T,?> parser) {
		return Parsers.withPost(this, parser);
	}
	
	default public Parser<T,R> afterIgnoring(Parser<T,?> parser) {
		return Parsers.withPre(this, parser);
	}
	
	default public <X> Parser<T,X> before(Parser<T,X> parser) {
		return Parsers.withPre(parser, this);
	}
	
	default public Parser<T,List<R>> many() {
		return Parsers.many(this);
	}
	
	default public Parser<T,List<R>> many1() {
		return Parsers.many1(this);
	}
	
	default public Parser<T,Optional<R>> maybe() {
		return Parsers.maybe(this);
	}
	
	default public Parser<T,List<R>> sepByEnd(Parser<T, ?> sep) {
	    return Parsers.sepByEnd(this, sep);
	}
	
	default public <X> Parser<T,X> apply(Function<R,X> function) {
		return Parsers.apply(this, function);
	}
	
	default public <X> Parser<T,X> become(X x) {
		return this.before(Parsers.success(x));
	}
	
	default public <X> Parser<T,ImmutablePair<R,X>> pairWith(Parser<T,X> parser) {
		return Parsers.pair(this, parser);
	}
	
	default public <X> Parser<T,ImmutablePair<R,X>> pairRight(X x) {
		return Parsers.pair(this, x);
	}
	
	default public <X> Parser<T,ImmutablePair<X,R>> pairLeft(X x) {
		return Parsers.pair(x, this);
	}
}
