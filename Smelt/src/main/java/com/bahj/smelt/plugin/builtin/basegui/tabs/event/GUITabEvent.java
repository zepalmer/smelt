package com.bahj.smelt.plugin.builtin.basegui.tabs.event;

import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITab;
import com.bahj.smelt.util.event.Event;

/**
 * The base class for GUI tab events.
 * 
 * @author Zachary Palmer
 */
public abstract class GUITabEvent implements Event {
    private GUITab tab;

    public GUITabEvent(GUITab tab) {
        super();
        this.tab = tab;
    }

    public GUITab getTab() {
        return tab;
    }

}
