package com.bahj.smelt.util.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 * This simple layout manager expects its target to have only one child. It forces the width of the child to be the
 * width of the target, but permits the child to be vertically smaller. The child is always positioned at (0,0), so any
 * additional space appears below the child.
 * 
 * @author Zachary Palmer
 */
public class WidthStretchLayout implements LayoutManager {
    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        if (parent.getComponentCount() == 0) {
            return new Dimension(0, 0);
        } else {
            return parent.getComponent(0).getPreferredSize();
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        if (parent.getComponentCount() == 0) {
            return new Dimension(0, 0);
        } else {
            return parent.getComponent(0).getMinimumSize();
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component component = parent.getComponent(i);
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            if (parent instanceof JComponent) {
                Border border = ((JComponent) parent).getBorder();
                if (border != null) {
                    Insets insets = border.getBorderInsets(parent);
                    left = insets.left;
                    top = insets.top;
                    right = insets.right;
                    bottom = insets.bottom;
                }
            }
            component.setLocation(left, top);
            if (i == 0) {
                component.setSize(parent.getWidth() - left - right,
                        (int) Math.min(component.getPreferredSize().getHeight(), parent.getHeight() - top - bottom));
            } else {
                component.setSize(0, 0);
            }
        }
    }
}
