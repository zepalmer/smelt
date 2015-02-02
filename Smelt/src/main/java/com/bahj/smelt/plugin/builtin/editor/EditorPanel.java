package com.bahj.smelt.plugin.builtin.editor;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.CWorkingArea;
import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.intern.CDockable;

import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITabKey;
import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
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

    /** The docking control used in this panel. */
    private CControl control;
    /** The working area in which editor panels will appear. */
    private CWorkingArea editorWorkingArea;

    public EditorPanel(JFrame owner, DataModelPlugin dataModelPlugin) {
        this.control = new CControl(owner);
        // TODO: call this.control.destroy() when appropriate (at least by the close of the application)?
        this.setLayout(new BorderLayout());
        this.add(control.getContentArea(), BorderLayout.CENTER);

        // Create the database browsing panel.
        EditorPanelContext context = new EditorPanelContextImpl();
        DatabaseTreeViewByTypePanel treeViewByTypePanel = new DatabaseTreeViewByTypePanel(dataModelPlugin, context);
        DefaultSingleCDockable treeViewByTypeDock = new DefaultSingleCDockable(TREE_VIEW_BY_TYPE_KEY,
                treeViewByTypePanel);
        treeViewByTypeDock.setTitleText("View by Type");
        treeViewByTypeDock.setExternalizable(false); // TODO: this feature is broken -- why?

        this.editorWorkingArea = control.createWorkingArea(EDITOR_WORKING_AREA_KEY);

        // Specify the position of the dockable space.
        CGrid grid = new CGrid(control);
        grid.add(0, 0, 1, 4, treeViewByTypeDock);
        grid.add(1, 0, 4, 4, editorWorkingArea);
        control.getContentArea().deploy(grid);
    }

    private class EditorPanelContextImpl implements EditorPanelContext {
        @Override
        public void openEditor(SmeltValue<?> value) {
            DefaultMultipleCDockable dockable = findEditorFor(value);
            if (dockable == null) {
                // The value does not currently have an editor. Create one.
                // TODO: actual editor component here
                JComponent editor = new JLabel(value.toString());

                // Set up a dockable for this editor
                DefaultMultipleCDockable editorDockable = new SmeltValueMultipleCDockable(value);
                editorDockable.setTitleText(value.toString()); // TODO: actual title text, and keep it in sync
                editorDockable.setCloseable(true);
                editorDockable.setRemoveOnClose(true);
                editorDockable.setExternalizable(false); // TODO: this feature is broken -- why?
                editorDockable.add(editor);

                // Present it
                EditorPanel.this.editorWorkingArea.show(editorDockable);
                editorDockable.toFront();
            } else {
                // There is already an editor for this value. Focus it.
                dockable.toFront();
                requestFocusInWindow();
            }
        }
    }

    private DefaultMultipleCDockable findEditorFor(SmeltValue<?> value) {
        for (int i=0;i<this.control.getCDockableCount();i++) {
            CDockable dockable = this.control.getCDockable(i);
            if (dockable instanceof SmeltValueMultipleCDockable) {
                SmeltValueMultipleCDockable smeltDockable = (SmeltValueMultipleCDockable)dockable;
                if (smeltDockable.getValue().equals(value)) {
                    return smeltDockable;
                }
            }
        }
        return null;
    }

    private static class SmeltValueMultipleCDockable extends DefaultMultipleCDockable {
        private SmeltValue<?> value;

        public SmeltValueMultipleCDockable(SmeltValue<?> value) {
            super(null);
            this.value = value;
        }

        public SmeltValue<?> getValue() {
            return value;
        }
    }
}
