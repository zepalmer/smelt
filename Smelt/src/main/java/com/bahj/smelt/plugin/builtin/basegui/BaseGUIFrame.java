package com.bahj.smelt.plugin.builtin.basegui;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.bahj.smelt.plugin.builtin.basegui.context.GUIConstructionContextImpl;
import com.bahj.smelt.plugin.builtin.basegui.context.SmeltBasicMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.context.SmeltMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.context.SmeltNestedMenu;

/**
 * The frame used by Smelt's base GUI plugin.
 * 
 * @author Zachary Palmer
 */
public class BaseGUIFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private SmeltTabPanel tabPanel;
    
    public BaseGUIFrame(GUIConstructionContextImpl context) {
        super("Smelt");

        // Set up the menu bar
        JMenuBar menuBar = new JMenuBar();
        Iterator<? extends SmeltMenuItem> iterator = context.getMenuBar().getMenuItems().stream().map(List::stream)
                .reduce(Stream.empty(), Stream::concat).iterator();
        while (iterator.hasNext()) {
            JMenu jmenu = (JMenu)createJMenuItemFromSmeltMenuItem(iterator.next());
            menuBar.add(jmenu);
        }
        this.setJMenuBar(menuBar);
        // TODO: set mnemonics
        
        // Set up the tab panel
        this.tabPanel = new SmeltTabPanel();
        this.setContentPane(tabPanel);
    }

    public SmeltTabPanel getTabPanel() {
        return tabPanel;
    }

    private static JMenuItem createJMenuItemFromSmeltMenuItem(SmeltMenuItem item) {
        if (item instanceof SmeltNestedMenu) {
            SmeltNestedMenu menu = (SmeltNestedMenu)item;
            JMenu jmenu = new JMenu(menu.getName());
            boolean firstGroup = true;
            for (List<? extends SmeltMenuItem> group : menu.getMenuItems()) {
                if (!firstGroup) {
                    jmenu.addSeparator();
                }
                for (SmeltMenuItem groupItem : group) {
                    JMenuItem jitem = createJMenuItemFromSmeltMenuItem(groupItem);
                    jmenu.add(jitem);
                }
                firstGroup = false;
            }
            return jmenu;
        } else if (item instanceof SmeltBasicMenuItem) {
            SmeltBasicMenuItem basicItem = (SmeltBasicMenuItem)item;
            JMenuItem jitem = new JMenuItem();
            jitem.setAction(basicItem.getAction());
            jitem.setText(basicItem.getName());
            return jitem;
        } else {
            throw new IllegalStateException("Unrecognized Smelt menu item type.");
        }
    }
}
