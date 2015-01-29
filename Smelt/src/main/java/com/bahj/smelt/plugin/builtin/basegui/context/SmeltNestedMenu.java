package com.bahj.smelt.plugin.builtin.basegui.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents a nested menu in a Smelt menu bar.
 * 
 * @author Zachary Palmer
 */
public class SmeltNestedMenu extends SmeltAbstractMenuItem {
    private List<List<? extends SmeltMenuItem>> menuItems;

    public SmeltNestedMenu(String name) {
        super(name);
        this.menuItems = new ArrayList<>();
    }

    public void addSingleMenuItem(SmeltMenuItem item) {
        this.addMenuItemGroup(Collections.singletonList(item));
    }

    public void addMenuItemGroup(List<? extends SmeltMenuItem> items) {
        this.menuItems.add(items);
    }

    /**
     * Looks up the first menu item in this menu by its name.
     * 
     * @param name
     *            The name to use.
     * @return The menu item (or <code>null</code> if no such item is found).
     */
    public SmeltMenuItem getMenuItemByName(String name) {
        // @formatter:off
        return this.menuItems
                .stream()
                .map(List::stream)
                .reduce(Stream::concat)
                .orElse(Stream.empty())
                .findFirst()
                .orElse(null);
        // @formatter:on
    }
    
    public List<List<? extends SmeltMenuItem>> getMenuItems() {
        return Collections.unmodifiableList(this.menuItems);
    }
}
