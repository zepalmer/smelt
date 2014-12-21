package com.bahj.flapjack;

import java.util.ListIterator;

public class ReadOnlyListIterator<T> implements ListIterator<T> {
	private ListIterator<T> it;

	public ReadOnlyListIterator(ListIterator<T> it) {
		super();
		this.it = it;
	}

	@Override
	public boolean hasNext() {
		return this.it.hasNext();
	}

	@Override
	public T next() {
		return this.it.next();
	}

	@Override
	public boolean hasPrevious() {
		return this.it.hasPrevious();
	}

	@Override
	public T previous() {
		return this.it.previous();
	}

	@Override
	public int nextIndex() {
		return this.it.nextIndex();
	}

	@Override
	public int previousIndex() {
		return this.it.previousIndex();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void set(T e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(T e) {
		throw new UnsupportedOperationException();
	}
}
