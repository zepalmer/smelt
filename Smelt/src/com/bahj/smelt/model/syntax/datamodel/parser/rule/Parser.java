package com.bahj.smelt.model.syntax.datamodel.parser.rule;

/**
 * A parser fragment in the Smelt parser.
 * 
 * @author Zachary Palmer
 *
 * @param <P>
 *            The type of input for this parser.
 * @param <R>
 *            The type of result produced from this parser.
 */
public interface Parser<P, R> {
	public R parse(P p);
}
