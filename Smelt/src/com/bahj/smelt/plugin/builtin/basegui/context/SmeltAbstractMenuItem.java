package com.bahj.smelt.plugin.builtin.basegui.context;

/**
 * An abstract type which handles common attributes of menu items.
 * @author Zachary Palmer
 */
public class SmeltAbstractMenuItem implements SmeltMenuItem {

    private String name;

    public SmeltAbstractMenuItem(String name) {
        super();
        this.name = name;
    }

    /**
     * The name of the menu item.
     * @return The menu item in question.
     */
    @Override
    public String getName() {
        return name;
    }

}