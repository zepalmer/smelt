package com.bahj.smelt.plugin.builtin.basegui.context;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.Action;

import com.bahj.smelt.plugin.builtin.basegui.menu.SmeltMenuItem;

/**
 * An interface for the context in which a Smelt GUI is constructed.
 * 
 * @author Zachary Palmer
 */
public interface GUIConstructionContext {
    /**
     * Adds a group of menu items to the menu bar of the Smelt GUI. If the menu does not yet exist, it is created.
     * Mnemonics are assigned automatically based on the keys which are currently available. The group will be separated
     * from other groups by standard menu separators.
     * 
     * @param menuName
     *            The name of the menu to which to add the item.
     * @param items
     *            The items to add.
     */
    public void addMenuItemGroup(String menuName, List<? extends SmeltMenuItem> items);

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
    public Action constructExecutionAction(Function<Action, Consumer<? super GUIExecutionContext>> actionFunction);
}