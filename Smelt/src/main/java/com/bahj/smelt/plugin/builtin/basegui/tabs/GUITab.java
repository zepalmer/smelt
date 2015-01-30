package com.bahj.smelt.plugin.builtin.basegui.tabs;

import javax.swing.JComponent;

import com.bahj.smelt.plugin.builtin.basegui.tabs.event.GUITabEvent;
import com.bahj.smelt.util.event.EventListener;

/**
 * Represents a single primary tab within the Smelt GUI.  A tab has a "key" (an unique identifying object), a title,
 * and a component to display.  It also receives tab events from the UI (such as closing notifications).
 * @author Zachary Palmer
 */
public interface GUITab extends EventListener<GUITabEvent> {
    public GUITabKey getKey();
    public String getTitle();
    public JComponent getContents();
}
