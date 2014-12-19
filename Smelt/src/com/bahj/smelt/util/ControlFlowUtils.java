package com.bahj.smelt.util;

public class ControlFlowUtils {
	/**
	 * Asserts that a value is not <code>null</code>.  If it is, a {@link NullPointerException} is thrown.
	 * @param t The value.
	 * @param message The message to yield in the event the value is <code>null</code>.
	 * @return The value, if it is not <code>null</code>.
	 * @throws NullPointerException If the value is <code>null</code>.
	 */
	public static <T> T assertNotNull(T t, String message) {
		if (t == null) {
			throw new NullPointerException(message);
		} else {
			return t;
		}
	}
}
