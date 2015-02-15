package com.bahj.smelt.plugin.builtin.basegui.construction.menu;

/**
 * An abstract type which handles common attributes of menu items.
 * @author Zachary Palmer
 */
public class SmeltAbstractMenuItem implements SmeltMenuItem {

    private String name;
    private Integer suggestedMnemonic;

    /**
     * Creates a new (abstract) menu item.  No mnemonic is suggested.
     * @param name The name of the item.
     */
    public SmeltAbstractMenuItem(String name) {
        this(name, null);
    }
    
    /**
     * Creates a new (abstract) menu item.
     * @param name The name of the item.
     * @param suggestedMnemonic The mnemonic suggested for this item, or <code>null</code> for no suggestion.
     */
    public SmeltAbstractMenuItem(String name, Integer suggestedMnemonic) {
        super();
        this.name = name;
        this.suggestedMnemonic = suggestedMnemonic;
    }


    /**
     * The name of the menu item.
     * @return The menu item in question.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the mnemonic suggested for this menu item.
     * @return The suggested mnemonic or <code>null</code> if no mnemonic is suggested.
     */
    public Integer getSuggestedMnemonic() {
        return suggestedMnemonic;
    }
}
