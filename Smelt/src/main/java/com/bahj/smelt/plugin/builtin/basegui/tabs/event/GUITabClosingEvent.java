package com.bahj.smelt.plugin.builtin.basegui.tabs.event;

import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITab;

/**
 * Indicates that a tab is closing.  This operation might be vetoable and, if it is, any recipient can set the veto flag
 * on this event to prevent the closure from occurring.
 * @author Zachary Palmer
 */
public class GUITabClosingEvent extends GUITabEvent {
    private boolean vetoable;
    private boolean vetoed;
    
    public GUITabClosingEvent(GUITab tab, boolean vetoable) {
        super(tab);
        this.vetoable = vetoable;
        this.vetoed = false;
    }
    
    public boolean isVetoable() {
        return this.vetoable;
    }
    
    public boolean isVetoed() {
        return this.vetoed;
    }
    
    /**
     * Vetoes this closing event.  If this closing event cannoted be vetoed, this method does nothing.
     */
    public void veto() {
        if (this.vetoable) {
            this.vetoed = true;
        }
    }
}
