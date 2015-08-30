package com.bahj.smelt.plugin.builtin.basegui.construction.menu;

import java.util.List;

import javax.swing.KeyStroke;

public interface SmeltMenuItem {

    /**
     * The name of the menu item.
     * 
     * @return The menu item in question.
     */
    public String getName();

    /**
     * Retrieves the mnemonics suggested for this menu item.
     * 
     * @return The suggested mnemonics, in the order in which they are preferred.
     */
    public List<Integer> getSuggestedMnemonics();
    
    /**
     * Adds a mnemonic to the list of suggestions.  It will take last priority.  This method has no effect if the
     * provided mnemonic is already attached to that menu item.
     */
    public void addSuggestedMnemonics(Integer... mnemonics);
    
    /**
     * Retrieves the suggested accelerator for this menu item.
     * @return The suggested accelerator as a {@link Keystroke} or <code>null</code> if no accelerator is set.
     */
    public KeyStroke getSuggestedAccelerator();
}