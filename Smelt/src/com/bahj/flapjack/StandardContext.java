package com.bahj.flapjack;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import com.nurkiewicz.lazyseq.LazySeq;

/**
 * A simple context implementation.
 * 
 * @author Zachary Palmer
 */
public class StandardContext<T> implements Context<T> {
	private Stack<ContextState> stateStack;

	public StandardContext(Iterator<T> tokens) {
		this.stateStack = new Stack<>();
		this.stateStack.push(new ContextState(LazySeq.of(tokens)));
	}

	@Override
	public T peek() {
		return this.stateStack.peek().getSeq().head();
	}

	@Override
	public T nextToken() {
		ContextState state = this.stateStack.peek();
		T t = state.getSeq().head();
		state.setSeq(state.getSeq().tail());
		state.markChanged();
		return t;
	}

	public ListIterator<T> currentTokens() {
		return new ReadOnlyListIterator<>(this.stateStack.peek().getSeq()
				.listIterator());
	}

	protected void markChanged() {
		this.stateStack.peek().markChanged();
	}

	@Override
	public void startTransaction() {
		this.stateStack.push(new ContextState(this.stateStack.peek().getSeq()));
	}

	@Override
	public void commit() {
		if (this.stateStack.size() < 2) {
			throw new IllegalStateException(
					"Cannot commit: no open transaction!");
		}
		ContextState toprs = this.stateStack.pop();
		ContextState currs = this.stateStack.peek();
		if (toprs.isChanged()) {
			currs.markChanged();
		}
		currs.setSeq(toprs.getSeq());
	}

	@Override
	public void rollback() {
		if (this.stateStack.size() < 2) {
			throw new IllegalStateException(
					"Cannot commit: no open transaction!");
		}
		this.stateStack.pop();
	}

	@Override
	public boolean hasChanged() {
		return this.stateStack.peek().isChanged();
	}

	private class ContextState {
		private LazySeq<T> value;
		private boolean changed;

		public ContextState(LazySeq<T> value) {
			super();
			this.value = value;
			this.changed = false;
		}

		public LazySeq<T> getSeq() {
			return value;
		}

		public void setSeq(LazySeq<T> value) {
			this.value = value;
		}

		public void markChanged() {
			this.changed = true;
		}

		public boolean isChanged() {
			return changed;
		}
	}
}
