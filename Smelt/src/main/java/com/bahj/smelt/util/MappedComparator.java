package com.bahj.smelt.util;

import java.util.Comparator;
import java.util.function.Function;

public class MappedComparator<T, U> implements Comparator<T> {
    private Function<T, U> mappingFunction;
    private Comparator<U> comparator;

    public MappedComparator(Function<T, U> mappingFunction, Comparator<U> comparator) {
        super();
        this.mappingFunction = mappingFunction;
        this.comparator = comparator;
    }

    @Override
    public int compare(T o1, T o2) {
        return comparator.compare(this.mappingFunction.apply(o1), this.mappingFunction.apply(o2));
    }
}
