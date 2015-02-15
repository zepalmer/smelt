package com.bahj.smelt.plugin.builtin.basegui.construction.menu;

import javax.swing.Action;

/**
 * Represents a single menu item in the Smelt GUI.
 * 
 * @author Zachary Palmer
 */
public class SmeltBasicMenuItem extends SmeltAbstractMenuItem {
    private Action action;

    public SmeltBasicMenuItem(String name, Action action) {
        super(name);
        this.action = action;
    }

    public SmeltBasicMenuItem(String name, Integer suggestedMnemonic, Action action) {
        super(name, suggestedMnemonic);
        this.action = action;
    }

    /**
     * The action to take when the menu item is selected.
     * @return The action to take.
     */
    public Action getAction() {
        return action;
    }
}
