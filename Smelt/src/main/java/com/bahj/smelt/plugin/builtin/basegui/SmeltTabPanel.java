package com.bahj.smelt.plugin.builtin.basegui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.bahj.smelt.plugin.builtin.basegui.context.GUITabKey;

public class SmeltTabPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Set<GUITabKey> keysUsed;
    private JTabbedPane tabbedPane;

    public SmeltTabPanel() {
        this.setMinimumSize(new Dimension(400,300));
        
        this.keysUsed = new HashSet<>();
        
        this.tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        this.setLayout(new BorderLayout());
        this.add(this.tabbedPane, BorderLayout.CENTER);
        
        
    }
    
    public void ensureTab(GUITabKey key, JComponent contents) {
        if (this.keysUsed.contains(key)) {
            return;
        }
        
        // Create a new tab containing the provided contents.
        this.tabbedPane.insertTab(key.getTitle(), null, contents, null, this.tabbedPane.getTabCount());
        
        // Change the header for the tab so that it has a closing 
    }
}
