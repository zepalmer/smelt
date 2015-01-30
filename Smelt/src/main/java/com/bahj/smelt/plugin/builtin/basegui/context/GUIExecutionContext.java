package com.bahj.smelt.plugin.builtin.basegui.context;

import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;
import com.bahj.smelt.plugin.builtin.basegui.SmeltTabPanel;
import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITab;

/**
 * An object permitting other plugins to interface with the GUI created by the {@link BaseGUIPlugin} at runtime.
 * @author Zachary Palmer
 */
public class GUIExecutionContext {
    private SmeltTabPanel tabPanel;
    
    public GUIExecutionContext(SmeltTabPanel tabPanel) {
        super();
        this.tabPanel = tabPanel;
    }

    /**
     * Ensures that a tab identified by the provided key exists.  If no such tab exists, one is created with the provided
     * contents.  If such a tab already exists, it is focused.
     * @param tab The tab in question.
     */
    public void ensureTab(GUITab tab) {
        this.tabPanel.ensureTab(tab);
    }
}
