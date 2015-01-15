package com.bahj.smelt.plugin.builtin.basegui.event;

import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;
import com.bahj.smelt.plugin.builtin.basegui.context.GUIConstructionContext;

/**
 * An event indicating that the base GUI plugin is intializing. When this is event is transmitted, the GUI has been
 * created but has not yet been packed or displayed. Plugins are expected to respond to this event by modifying the GUI
 * objects retained by the {@link BaseGUIPlugin} to include their UI components.
 * 
 * @author Zachary Palmer
 */
public class BaseGUIInitializingEvent extends BaseGUIEvent {
    private GUIConstructionContext context;

    public BaseGUIInitializingEvent(GUIConstructionContext context) {
        super();
        this.context = context;
    }

    public GUIConstructionContext getContext() {
        return context;
    }
}
