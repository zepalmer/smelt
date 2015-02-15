package com.bahj.smelt.plugin.builtin.basegui;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.CWorkingArea;
import bibliothek.gui.dock.common.theme.ThemeMap;

import com.bahj.smelt.plugin.builtin.basegui.construction.GUIConstructionContextImpl;
import com.bahj.smelt.plugin.builtin.basegui.construction.menu.SmeltBasicMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.construction.menu.SmeltMenuItem;
import com.bahj.smelt.plugin.builtin.basegui.construction.menu.SmeltNestedMenu;
import com.bahj.smelt.plugin.builtin.basegui.execution.PlacementContext;

/**
 * The frame used by Smelt's base GUI plugin.
 * 
 * @author Zachary Palmer
 */
public class BaseGUIFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final String PRIMARY_WORKING_AREA_KEY = "working-area";

    private CControl dockingControl;
    private CWorkingArea primaryWorkingArea;

    public BaseGUIFrame(GUIConstructionContextImpl context) {
        super("Smelt");

        // Set up the menu bar
        JMenuBar menuBar = new JMenuBar();
        Iterator<? extends SmeltMenuItem> iterator = context.getMenuBar().getMenuItems().stream().map(List::stream)
                .reduce(Stream.empty(), Stream::concat).iterator();
        while (iterator.hasNext()) {
            JMenu jmenu = (JMenu) createJMenuItemFromSmeltMenuItem(iterator.next());
            menuBar.add(jmenu);
        }
        this.setJMenuBar(menuBar);
        // TODO: set mnemonics

        // Set up the docking space.
        this.dockingControl = new CControl(this);
        this.primaryWorkingArea = this.dockingControl.createWorkingArea(PRIMARY_WORKING_AREA_KEY);
        
        // Add it to the primary frame.
        this.setContentPane(this.dockingControl.getContentArea());
        
        // Configure default appearance of the primary working area.
        this.primaryWorkingArea.setVisible(true);
        this.primaryWorkingArea.setLocation(CLocation.base().normalEast(0.75));
        
        // Set to an Eclipse-like theme.
        this.dockingControl.setTheme(ThemeMap.KEY_ECLIPSE_THEME);
    }

    public PlacementContext getPlacementContext() {
        return new PlacementContext(dockingControl, primaryWorkingArea);
    }

    private static JMenuItem createJMenuItemFromSmeltMenuItem(SmeltMenuItem item) {
        if (item instanceof SmeltNestedMenu) {
            SmeltNestedMenu menu = (SmeltNestedMenu) item;
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
            SmeltBasicMenuItem basicItem = (SmeltBasicMenuItem) item;
            JMenuItem jitem = new JMenuItem();
            jitem.setAction(basicItem.getAction());
            jitem.setText(basicItem.getName());
            return jitem;
        } else {
            throw new IllegalStateException("Unrecognized Smelt menu item type.");
        }
    }
}
