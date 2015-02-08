package com.bahj.smelt.plugin.builtin.editor;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.CWorkingArea;
import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.event.CDockableStateListener;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.mode.ExtendedMode;

import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITabKey;
import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseClosedEvent;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltTypeMismatchException;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.editor.forms.Form;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactory;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactoryRegistry;
import com.bahj.smelt.plugin.builtin.editor.views.treebytype.DatabaseTreeViewByTypePanel;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;
import com.bahj.smelt.util.swing.WidthStretchPanel;

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

    /** The registry containing the form factories for this panel. */
    private FormFactoryRegistry formFactoryRegistry;

    /** The docking control used in this panel. */
    private CControl control;
    /** The working area in which editor panels will appear. */
    private CWorkingArea editorWorkingArea;

    /** The set of editors to close if the database is closed. */
    private Set<DefaultMultipleCDockable> toCloseOnDatabaseClose;

    public EditorPanel(JFrame owner, DataModelPlugin dataModelPlugin, FormFactoryRegistry formFactoryRegistry) {
        this.formFactoryRegistry = formFactoryRegistry;

        this.toCloseOnDatabaseClose = new HashSet<>();

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

        // Make sure that editor panels are properly destroyed if the database is closed.
        dataModelPlugin.addListener(new TypedEventListener<>(DatabaseClosedEvent.class,
                new EventListener<DatabaseClosedEvent>() {
                    @Override
                    public void eventOccurred(DatabaseClosedEvent event) {
                        for (DefaultMultipleCDockable editorDockable : toCloseOnDatabaseClose) {
                            control.removeDockable(editorDockable);
                        }
                        toCloseOnDatabaseClose.clear();
                    }
                }));
    }

    private class EditorPanelContextImpl implements EditorPanelContext {
        @Override
        public <V extends SmeltValue<V>> void openEditor(V value) {
            DefaultMultipleCDockable dockable = findEditorFor(value);
            if (dockable == null) {
                // The value does not currently have an editor. Create one.
                FormFactory formFactory = EditorPanel.this.formFactoryRegistry.getFormFactory(value.getType());
                if (formFactory == null) {
                    // TODO: generate some kind of panel indicating that no form was specified for this datum's type.
                    throw new NotYetImplementedException("No form factory for provided value's type: "
                            + value.getType());
                }
                Form form;
                try {
                    form = formFactory.createForm(value);
                } catch (SmeltTypeMismatchException e) {
                    // This indicates a malfunction in the form logic; a form should not be able to reject its own type
                    // of data.
                    throw new IllegalStateException("Form rejected its own data type for value: " + value);
                }
                JComponent editor = form.getComponent();
                WidthStretchPanel panel = new WidthStretchPanel(editor);
                panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // TODO: constant somewhere?
                JScrollPane editorScrollPane = new JScrollPane(panel);

                // Set up a dockable for this editor
                DefaultMultipleCDockable editorDockable = new SmeltValueMultipleCDockable(value);
                editorDockable.setTitleText(value.toString()); // TODO: actual title text, and keep it in sync
                editorDockable.setCloseable(true);
                editorDockable.setRemoveOnClose(true);
                editorDockable.setExternalizable(false); // TODO: this feature is broken -- why?
                editorDockable.add(editorScrollPane);

                // Add listener to destroy form when window is closed
                editorDockable.addCDockableStateListener(new CDockableStateListener() {
                    @Override
                    public void visibilityChanged(CDockable dockable) {
                        if (!dockable.isVisible()) {
                            form.destroy();
                            control.removeDockable(editorDockable);
                            EditorPanel.this.toCloseOnDatabaseClose.remove(editorDockable);
                        }
                    }

                    @Override
                    public void extendedModeChanged(CDockable dockable, ExtendedMode mode) {
                    }
                });

                // Add this form to the on-database-close set.
                EditorPanel.this.toCloseOnDatabaseClose.add(editorDockable);

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
        for (int i = 0; i < this.control.getCDockableCount(); i++) {
            CDockable dockable = this.control.getCDockable(i);
            if (dockable instanceof SmeltValueMultipleCDockable) {
                SmeltValueMultipleCDockable smeltDockable = (SmeltValueMultipleCDockable) dockable;
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
