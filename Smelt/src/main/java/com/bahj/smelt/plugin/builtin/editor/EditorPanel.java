package com.bahj.smelt.plugin.builtin.editor;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.CWorkingArea;
import bibliothek.gui.dock.common.DefaultSingleCDockable;

import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITabKey;
import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.editor.views.treebytype.DatabaseTreeViewByTypePanel;

/**
 * The primary panel displayed by the editor plugin for database editing.
 * 
 * @author Zachary Palmer
 */
public class EditorPanel extends JPanel {
    /**
     * A singleton representing the key for this editor panel.
     * 
     * @author Zachary Palmer
     */
    public static enum Key implements GUITabKey {
        INSTANCE
    }

    private static final long serialVersionUID = 1L;

    private static final String TREE_VIEW_BY_TYPE_KEY = "tree-view-by-type";
    private static final String EDITOR_WORKING_AREA_KEY = "editor-area";

    public EditorPanel(JFrame owner, DataModelPlugin dataModelPlugin) {
        // Create the dockable control which we will use as the contents of this panel.
        CControl control = new CControl(owner);
        // TODO: call this.control.destroy() when appropriate (at least by the close of the application)?
        this.setLayout(new BorderLayout());
        this.add(control.getContentArea(), BorderLayout.CENTER);

        // Create the database browsing panel.
        DatabaseTreeViewByTypePanel treeViewByTypePanel = new DatabaseTreeViewByTypePanel(dataModelPlugin);
        DefaultSingleCDockable treeViewByTypeDock = new DefaultSingleCDockable(TREE_VIEW_BY_TYPE_KEY,
                treeViewByTypePanel);
        treeViewByTypeDock.setTitleText("View by Type");

        // Create the working area where editors will appear.
        CWorkingArea work = control.createWorkingArea(EDITOR_WORKING_AREA_KEY);

        // Specify the position of the dockable space.
        CGrid grid = new CGrid(control);
        grid.add(0, 0, 1, 4, treeViewByTypeDock);
        grid.add(1, 0, 4, 4, work);
        control.getContentArea().deploy(grid);

        // TODO: something which causes editor windows to appear!
    }
}
