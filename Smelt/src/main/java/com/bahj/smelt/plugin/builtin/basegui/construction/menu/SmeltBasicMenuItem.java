package com.bahj.smelt.plugin.builtin.basegui.construction.menu;

import java.awt.Event;

import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 * Represents a single menu item in the Smelt GUI.
 * 
 * @author Zachary Palmer
 */
public class SmeltBasicMenuItem extends SmeltAbstractMenuItem {
    private Action action;

    /**
     * A convenience constructor which, for a given key code, suggests that code as a mnemonic and Ctrl+ that code as an
     * accelerator.
     * 
     * @param name
     *            The name of the item.
     * @param action
     *            The action associated with the item.
     * @param keyCode
     *            The key code to suggest.
     */
    public SmeltBasicMenuItem(String name, Action action, Integer keyCode) {
        this(name, action, KeyStroke.getKeyStroke(keyCode, Event.CTRL_MASK), keyCode);
    }

    public SmeltBasicMenuItem(String name, Action action, KeyStroke accelerator, Integer... suggestedMnemonics) {
        super(name, accelerator, suggestedMnemonics);
        this.action = action;
    }

    /**
     * The action to take when the menu item is selected.
     * 
     * @return The action to take.
     */
    public Action getAction() {
        return action;
    }
}
