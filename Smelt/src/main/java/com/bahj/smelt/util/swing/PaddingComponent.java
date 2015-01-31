package com.bahj.smelt.util.swing;

import java.awt.Dimension;

import javax.swing.JComponent;

/**
 * A simple component which prefers a fixed size and has no contents.
 * 
 * @author Zachary Palmer
 */
public class PaddingComponent extends JComponent {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a square padding component of the specified size.
     */
    public PaddingComponent(int size) {
        this(size, size);
    }

    /**
     * Creates a rectangular padding component of the specified size.
     */
    public PaddingComponent(int width, int height) {
        this(new Dimension(width, height));
    }

    /**
     * Creates a padding component of the specified size.
     * 
     * @param size
     *            The size in question.
     */
    public PaddingComponent(Dimension size) {
        this.setPreferredSize(size);
    }
}
