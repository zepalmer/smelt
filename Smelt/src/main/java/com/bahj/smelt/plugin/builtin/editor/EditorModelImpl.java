package com.bahj.smelt.plugin.builtin.editor;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockable;
import bibliothek.gui.dock.common.event.CDockableStateListener;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.mode.ExtendedMode;

import com.bahj.smelt.plugin.builtin.basegui.execution.BasicGUIPosition;
import com.bahj.smelt.plugin.builtin.basegui.execution.DockableIdentifier;
import com.bahj.smelt.plugin.builtin.basegui.execution.GUIExecutionContext;
import com.bahj.smelt.plugin.builtin.data.model.DataModelPlugin;
import com.bahj.smelt.plugin.builtin.data.model.event.DataModelPluginEvent;
import com.bahj.smelt.plugin.builtin.data.model.event.DatabaseClosedEvent;
import com.bahj.smelt.plugin.builtin.data.model.type.SmeltTypeMismatchException;
import com.bahj.smelt.plugin.builtin.data.model.value.SmeltValue;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueDescriptionUpdateEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltValueEvent;
import com.bahj.smelt.plugin.builtin.editor.forms.Form;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactory;
import com.bahj.smelt.plugin.builtin.editor.forms.FormFactoryRegistry;
import com.bahj.smelt.util.NotYetImplementedException;
import com.bahj.smelt.util.ReadableStrongReference;
import com.bahj.smelt.util.event.EventListener;
import com.bahj.smelt.util.event.TypedEventListener;
import com.bahj.smelt.util.swing.WidthStretchPanel;

/**
 * A class tracking the UI state of the editor plugin, specifically with respect to the editors it has created.
 * 
 * @author Zachary Palmer
 */
public class EditorModelImpl implements EditorModel {
    /** The number of unit increments per movement of e.g. the mouse wheel when scrolling an editor panel. */
    private static final int SCROLL_SPEED = 16;
    // TODO: consider extracting this to a user preference (once user preferences exist)
    
    private DataModelPlugin dataModelPlugin;
    private FormFactoryRegistry formFactoryRegistry;
    private ReadableStrongReference<GUIExecutionContext> guiExecutionContextRef;

    /**
     * Creates an empty editor model.
     * 
     * @param dataModelPlugin
     *            The plugin handling the data model (so that the database can be observed).
     * @param formFactoryRegistry
     *            The form factory to use when generating forms for values.
     * @param contextRef
     *            A reference which will hold the execution context for this GUI by the time editors are presented.
     */
    public EditorModelImpl(DataModelPlugin dataModelPlugin, FormFactoryRegistry formFactoryRegistry,
            ReadableStrongReference<GUIExecutionContext> contextRef) {
        super();
        this.dataModelPlugin = dataModelPlugin;
        this.formFactoryRegistry = formFactoryRegistry;
        this.guiExecutionContextRef = contextRef;
    }

    public <V extends SmeltValue<V, E>, E extends SmeltValueEvent<V, E>> void openEditor(V value) {
        if (this.guiExecutionContextRef.getValue() == null) {
            // TODO: appropriate ISE - the caller is at fault
            throw new NotYetImplementedException("no runtime context");
        }
        GUIExecutionContext guiExecutionContext = guiExecutionContextRef.getValue();

        DockableIdentifier identifier = new SmeltValueEditorDockableIdentifier(value);

        MultipleCDockable dockable = guiExecutionContext.getDockable(identifier);
        if (dockable == null) {
            // The value does not currently have an editor. Create one.
            FormFactory formFactory = this.formFactoryRegistry.getFormFactory(value.getType());
            if (formFactory == null) {
                // TODO: generate some kind of panel indicating that no form was specified for this datum's type.
                throw new NotYetImplementedException("No form factory for provided value's type: " + value.getType());
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
            editorScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);

            // Set up a dockable for this editor
            DefaultMultipleCDockable editorDockable = new DefaultMultipleCDockable(null);
            editorDockable.setTitleText(value.getDescription());
            editorDockable.setCloseable(true);
            editorDockable.setExternalizable(false); // TODO: this feature is broken -- why?
            editorDockable.add(editorScrollPane);

            // Keep dockable title text in sync.
            EventListener<SmeltValueEvent<?, ?>> descriptionUpdateListener = new EventListener<SmeltValueEvent<?, ?>>() {
                @Override
                public void eventOccurred(SmeltValueEvent<?, ?> event) {
                    if (event instanceof SmeltValueDescriptionUpdateEvent) {
                        editorDockable.setTitleText(value.getDescription());
                    }
                }
            };
            value.addListener(descriptionUpdateListener);

            // Create a listener to close the window when the database is closed.
            EventListener<DataModelPluginEvent> closeWindowOnDatabaseCloseListener = new TypedEventListener<>(
                    DatabaseClosedEvent.class, new EventListener<DatabaseClosedEvent>() {
                        @Override
                        public void eventOccurred(DatabaseClosedEvent event) {
                            editorDockable.setVisible(false);
                        }
                    });
            dataModelPlugin.addListener(closeWindowOnDatabaseCloseListener);

            // Add listener to destroy form when window is closed
            editorDockable.addCDockableStateListener(new CDockableStateListener() {
                @Override
                public void visibilityChanged(CDockable dockable) {
                    if (!dockable.isVisible()) {
                        form.destroy();
                        value.removeListener(descriptionUpdateListener);
                        dataModelPlugin.removeListener(closeWindowOnDatabaseCloseListener);
                        guiExecutionContext.removeDockable(editorDockable);
                    }
                }

                @Override
                public void extendedModeChanged(CDockable dockable, ExtendedMode mode) {
                }
            });

            // Present it
            guiExecutionContext.addDockable(editorDockable, BasicGUIPosition.PRIMARY, identifier);
            editorDockable.toFront();
        } else {
            // There is already an editor for this value. Focus it.
            if (dockable instanceof DefaultMultipleCDockable) {
                ((DefaultMultipleCDockable) dockable).toFront();
            }
        }
    }

    /**
     * Represents a unique identifier type for each {@link SmeltValue} which can be edited. This identifier relies on
     * the (widely-used) property of Smelt values that they do not support equality or hash code operations beyond those
     * provided by the JVM.
     * 
     * @author Zachary Palmer
     */
    private static class SmeltValueEditorDockableIdentifier implements DockableIdentifier {
        private SmeltValue<?, ?> value;

        public SmeltValueEditorDockableIdentifier(SmeltValue<?, ?> value) {
            super();
            this.value = value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SmeltValueEditorDockableIdentifier other = (SmeltValueEditorDockableIdentifier) obj;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }
    }
}
