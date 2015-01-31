package com.bahj.smelt.plugin.builtin.editor;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITabKey;

/**
 * The primary panel displayed by the editor plugin for database editing.
 * @author Zachary Palmer
 */
public class EditorPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    public static enum Key implements GUITabKey { INSTANCE }
    
    public EditorPanel() {
        // TODO
        // TODO: remove this temporary junk
        this.setLayout(new FlowLayout());
        this.add(new JLabel("Label!"));
        this.add(new JButton("Button!"));
    }

}
