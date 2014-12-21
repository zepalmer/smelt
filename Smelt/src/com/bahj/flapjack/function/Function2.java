package com.bahj.flapjack.function;

import java.util.function.Function;

/**
 * An uncurried two-argument form of {@link Function}.
 * @author Zachary Palmer
 *
 * @param <P1> The first parameter type.
 * @param <P2> The second parameter type.
 * @param <R> The return type.
 */
public interface Function2<P1,P2,R> {
	public R apply(P1 p1, P2 p2);
}
