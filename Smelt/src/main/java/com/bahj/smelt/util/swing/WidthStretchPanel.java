package com.bahj.smelt.util.swing;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JPanel;

/**
 * A panel which stretches the provided child in width to meet its own width. The height of the child remains flexible.
 * 
 * @author Zachary Palmer
 */
public class WidthStretchPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public WidthStretchPanel(Component component) {
        super();
        this.setLayout(new BorderLayout());
        this.add(component, BorderLayout.NORTH);
        this.add(Box.createVerticalGlue(), BorderLayout.CENTER);
    }
}
