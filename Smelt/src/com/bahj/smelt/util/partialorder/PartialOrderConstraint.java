package com.bahj.smelt.util.partialorder;

/**
 * An object describing a partial ordering constraint for a given type. This indicates a strict less-than relationship
 * between two elements.
 * 
 * @author Zachary Palmer
 * @param <T>
 *            The type for which this constraint applies.
 */
public class PartialOrderConstraint<T> {
    private T less;
    private T greater;

    /**
     * Accepts two values, the first of which is constrained to be (strictly) less than the second.
     * 
     * @param less
     * @param greater
     */
    public PartialOrderConstraint(T less, T greater) {
        super();
        this.less = less;
        this.greater = greater;
    }

    public T getLess() {
        return less;
    }

    public T getGreater() {
        return greater;
    }

}
