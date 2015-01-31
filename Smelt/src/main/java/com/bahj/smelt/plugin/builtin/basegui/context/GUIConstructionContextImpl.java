package com.bahj.smelt.plugin.builtin.basegui.context;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;
import com.bahj.smelt.plugin.builtin.basegui.menu.SmeltMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.menu.SmeltNestedMenu;
import com.bahj.smelt.util.StrongReference;

/**
 * An object representing the form of the GUI as it is being created by the {@link BaseGUIPlugin}.
 * 
 * @author Zachary Palmer
 */
public class GUIConstructionContextImpl implements GUIConstructionContext {
    private StrongReference<GUIExecutionContext> executionContextReference = new StrongReference<GUIExecutionContext>(
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
    public Action constructExecutionAction(final Function<Action, Consumer<? super GUIExecutionContext>> actionFunction) {
        GUIAction action = new GUIAction();
        action.setBehavior(actionFunction.apply(action));
        return action;
    }

    public StrongReference<GUIExecutionContext> getExecutionContextReference() {
        return executionContextReference;
    }

    public SmeltNestedMenu getMenuBar() {
        return menuBar;
    }

    private class GUIAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        
        private Consumer<? super GUIExecutionContext> behavior = null;

        public void setBehavior(Consumer<? super GUIExecutionContext> behavior) {
            this.behavior = behavior;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.behavior.accept(executionContextReference.getValue());
        }
    }
}
