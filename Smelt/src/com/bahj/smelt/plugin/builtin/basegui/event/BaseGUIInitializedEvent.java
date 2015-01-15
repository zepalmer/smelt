package com.bahj.smelt.plugin.builtin.basegui.event;


/**
 * An event indicating that the base GUI plugin has finished initializing. When this is event is transmitted, the GUI
 * has been created, packed, and is prepared to be displayed. All plugins have been given an opportunity to modify the
 * GUI. This event is fired after the {@link BaseGUIInitializingEvent}.
 * 
 * @author Zachary Palmer
 */
public class BaseGUIInitializedEvent extends BaseGUIEvent {
}
