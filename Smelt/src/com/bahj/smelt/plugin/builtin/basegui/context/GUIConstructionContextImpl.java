package com.bahj.smelt.plugin.builtin.basegui.context;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;
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
    public Action constructExecutionAction(final Consumer<? super GUIExecutionContext> actionFunction) {
        return new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                actionFunction.accept(executionContextReference.getValue());
            }
        };
    }

    public StrongReference<GUIExecutionContext> getExecutionContextReference() {
        return executionContextReference;
    }

    public SmeltNestedMenu getMenuBar() {
        return menuBar;
    }
}
