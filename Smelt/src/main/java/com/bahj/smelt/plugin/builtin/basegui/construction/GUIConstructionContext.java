package com.bahj.smelt.plugin.builtin.basegui.construction;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Action;

import com.bahj.smelt.plugin.builtin.basegui.construction.menu.SmeltMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.execution.GUIExecutionContext;

/**
 * An interface for the context in which a Smelt GUI is constructed.
 * 
 * @author Zachary Palmer
 */
public interface GUIConstructionContext {
    /**
     * Adds a group of menu items to the menu bar of the Smelt GUI. If the menu does not yet exist, it is created.
     * The group will be separated from other groups by standard menu separators.
     * 
     * @param menuName
     *            The name of the menu to which to add the item.
     * @param items
     *            The items to add.
     */
    public void addMenuItemGroup(String menuName, List<? extends SmeltMenuItem> items);

    /**
     * Adds a group of menu items to the menu bar of the Smelt GUI. If the menu does not yet exist, it is created.
     * The group will be separated from other groups by standard menu separators.
     * 
     * @param menuName
     *            The name of the menu to which to add the item.
     * @param items
     *            The items to add.
     */
    default public void addMenuItemGroup(String menuName, SmeltMenuItem... items) {
        addMenuItemGroup(menuName, Arrays.asList(items));
    }
    
    /**
     * Adds mnemonic suggestions for a given menu.  Mnemonics are assigned to menus on a first-come first-serve basis,
     * but menus may keep a list of alternatives in the event that their preferred mnemonics are consumed.  If a
     * mnemonic has already been suggested for a menu, suggesting it again has no effect.
     * @param menuName The name of the menu.
     * @param mnemonics The mnemonics to suggest for it.
     */
    public void addMenuMnemonicSuggestion(String menuName, Integer... mnemonics);
    
    /**
     * Creates an {@link Action} based on a {@link GUIExecutionContext}. This allows the behavior of plugins' GUI
     * components to be defined independent of the concrete GUI but in reference to GUI-related functionality exposed
     * through the context. The constructed action must not be triggered before the completion of GUI setup.
     * 
     * @param actionFunction
     *            A function accepting the action being constructed (for purposes of self-reference) and returning the
     *            consumer which should be executed when the action is taken.
     * @return The resulting action.
     */
    public Action constructExecutionAction(Consumer<? super GUIExecutionContext> actionFunction);
}