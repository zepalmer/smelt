package com.bahj.smelt.plugin.builtin.basegui.construction;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;
import com.bahj.smelt.plugin.builtin.basegui.construction.menu.SmeltMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.construction.menu.SmeltNestedMenu;
import com.bahj.smelt.plugin.builtin.basegui.execution.GUIExecutionContext;
import com.bahj.smelt.util.StrongReference;
import com.bahj.smelt.util.StrongReferenceImpl;

/**
 * An object representing the form of the GUI as it is being created by the {@link BaseGUIPlugin}.
 * 
 * @author Zachary Palmer
 */
public class GUIConstructionContextImpl implements GUIConstructionContext {
    private StrongReference<GUIExecutionContext> executionContextReference = new StrongReferenceImpl<GUIExecutionContext>(
            null);
    private SmeltNestedMenu menuBar = new SmeltNestedMenu(null);

    @Override
    public void addMenuItemGroup(String menuName, List<? extends SmeltMenuItem> items) {
        SmeltNestedMenu menu = (SmeltNestedMenu) menuBar.getMenuItemByName(menuName);
        if (menu == null) {
            menu = new SmeltNestedMenu(menuName);
            menuBar.addSingleMenuItem(menu);
        }
        menu.addMenuItemGroup(items);
    }

    @Override
    public void addMenuMnemonicSuggestion(String menuName, Integer... mnemonics) {
        SmeltNestedMenu menu = (SmeltNestedMenu) menuBar.getMenuItemByName(menuName);
        if (menu == null) {
            menu = new SmeltNestedMenu(menuName);
            menuBar.addSingleMenuItem(menu);
        }
        menu.addSuggestedMnemonics(mnemonics);
    }

    @Override
    public Action constructExecutionAction(Consumer<? super GUIExecutionContext> actionFunction) {
        return new GUIAction(actionFunction);
    }

    public StrongReference<GUIExecutionContext> getExecutionContextReference() {
        return executionContextReference;
    }

    public SmeltNestedMenu getMenuBar() {
        return menuBar;
    }

    private class GUIAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private Consumer<? super GUIExecutionContext> behavior;

        public GUIAction(Consumer<? super GUIExecutionContext> behavior) {
            super();
            this.behavior = behavior;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.behavior.accept(executionContextReference.getValue());
        }
    }
}
