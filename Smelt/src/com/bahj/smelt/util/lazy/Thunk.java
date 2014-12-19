package com.bahj.smelt.util.lazy;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An object which represents a lazy computation. The computation is expressed
 * in the form of a {@link Function} of no arguments. The result of this
 * function is stored locally and reused on future evaluations.
 * 
 * @author Zachary Palmer
 */
public class Thunk<T> implements Supplier<T> {
	private Supplier<T> lazy;
	private T value;

	public Thunk(Function<Void, T> f) {
		this(makeSupplierFromFunction(f));
	}
	
	private static <U> Supplier<U> makeSupplierFromFunction(Function<Void, U> f) {
		if (f==null) {
			throw new NullPointerException("Thunk function cannot be null!");
		}
		return (() -> f.apply(null));
	}
	
	public Thunk(Supplier<T> lazy) {
		if (lazy == null) {
			throw new NullPointerException("Thunk lazy value cannot be null!");
		}
		this.lazy = lazy;
	}

	public T get() {
		if (this.lazy != null) {
			this.value = this.lazy.get();
			this.lazy = null;
		}
		return this.value;
	}
}
