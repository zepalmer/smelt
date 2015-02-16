package com.bahj.smelt.util.swing;

import java.awt.Component;

import javax.swing.JPanel;

import com.bahj.smelt.util.swing.layout.WidthStretchLayout;

/**
 * A panel which stretches the provided child in width to meet its own width. The height of the child is left flexible.
 * This panel is merely a convenience mechanism for constructing a {@link JPanel} with a single child and a
 * {@link WidthStretchLayout}.
 * 
 * @author Zachary Palmer
 */
public class WidthStretchPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public WidthStretchPanel(Component component) {
        super();
        this.setLayout(new WidthStretchLayout());
        this.add(component);
    }
}
