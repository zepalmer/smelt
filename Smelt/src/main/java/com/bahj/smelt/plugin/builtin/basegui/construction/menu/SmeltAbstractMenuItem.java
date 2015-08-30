package com.bahj.smelt.plugin.builtin.basegui.construction.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.KeyStroke;

/**
 * An abstract type which handles common attributes of menu items.
 * 
 * @author Zachary Palmer
 */
public class SmeltAbstractMenuItem implements SmeltMenuItem {

    private String name;
    private List<Integer> suggestedMnemonics;
    private KeyStroke accelerator;

    /**
     * Creates a new (abstract) menu item.
     * 
     * @param name
     *            The name of the item.
     * @param accelerator
     *            The accelerator keystroke for this item, or <code>null</code> for no accelerator.
     * @param suggestedMnemonic
     *            The mnemonic suggested for this item, or <code>null</code> for no suggestion.
     */
    public SmeltAbstractMenuItem(String name, KeyStroke accelerator, Integer... suggestedMnemonics) {
        super();
        this.name = name;
        this.accelerator = accelerator;
        this.suggestedMnemonics = new ArrayList<>(Arrays.asList(suggestedMnemonics));
    }

    /**
     * The name of the menu item.
     * 
     * @return The menu item in question.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the mnemonics suggested for this menu item.
     * 
     * @return The suggested mnemonics, in the order in which they are preferred.
     */
    public List<Integer> getSuggestedMnemonics() {
        return Collections.unmodifiableList(suggestedMnemonics);
    }

    @Override
    public void addSuggestedMnemonics(Integer... mnemonics) {
        for (Integer mnemonic : mnemonics) {
            if (!this.suggestedMnemonics.contains(mnemonic)) {
                this.suggestedMnemonics.add(mnemonic);
            }
        }
    }

    public KeyStroke getSuggestedAccelerator() {
        return accelerator;
    }

}
