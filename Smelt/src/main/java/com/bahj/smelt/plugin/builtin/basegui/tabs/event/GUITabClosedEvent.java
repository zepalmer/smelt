package com.bahj.smelt.plugin.builtin.basegui.tabs.event;

import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITab;

/**
 * Dispatched after a GUI tab has been closed.
 * @author Zachary Palmer
 */
public class GUITabClosedEvent extends GUITabEvent {
    public GUITabClosedEvent(GUITab tab) {
        super(tab);
    }
}
