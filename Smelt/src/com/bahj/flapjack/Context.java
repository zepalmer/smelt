package com.bahj.flapjack;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Represents a parser context. This context is manipulated by parsers in order
 * to receive tokens. Contexts are transactional in the form of a stack: the
 * state of a context may be duplicated (adding an element to the stack) and
 * then either committed (which eliminates the next-to-last element) or rolled
 * back (which eliminates the last element). Clients are obligated to ensure
 * that each duplication is matched by a rollback or a commit.
 * 
 * @author Zachary Palmer
 *
 * @param <T>
 *            The type of tokens for this context.
 */
public interface Context<T> {
	/**
	 * Examines the next token in context. This token is <i>not</i> consumed.
	 * 
	 * @return The next token.
	 * @throws NoSuchElementException
	 *             If no tokens remain.
	 */
	public T peek();

	/**
	 * Obtains the next token in context. This token is consumed.
	 * 
	 * @return The next token.
	 * @throws NoSuchElementException
	 *             If no tokens remain.
	 */
	public T nextToken();
	
	/**
	 * Obtains an immutable iterator over the current tokens.
	 * @reutrn The current tokens in this context.
	 */
	public ListIterator<T> currentTokens();

	/**
	 * Starts a transaction, effectively marking the current state of this
	 * context.
	 */
	public void startTransaction();

	/**
	 * Commits the changes since the last transaction.
	 */
	public void commit();

	/**
	 * Discards the changes since the last transaction.
	 */
	public void rollback();

	/**
	 * Determines if this context has changed since the last transaction start.
	 * 
	 * @return <code>true</code> if the context has changed; <code>false</code>
	 *         if it hasn't.
	 */
	public boolean hasChanged();
}
