package com.bahj.smelt.plugin.builtin.basegui.execution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockable;
import bibliothek.gui.dock.common.event.CDockableStateListener;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.mode.ExtendedMode;

import com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin;

/**
 * An object permitting other plugins to interface with the GUI created by the {@link BaseGUIPlugin} at runtime.
 * 
 * @author Zachary Palmer
 */
public class GUIExecutionContext {
    private PlacementContext placementContext;

    private Map<DockableIdentifier, DockableEntry> dockableMap;
    private Map<MultipleCDockable, Set<DockableIdentifier>> reverseDockableMap;

    public GUIExecutionContext(PlacementContext placementContext) {
        super();
        this.placementContext = placementContext;
        this.dockableMap = new HashMap<>();
        this.reverseDockableMap = new HashMap<>();
    }

    /**
     * Either creates or displays a {@link MultipleCDockable}, as appropriate. If a dockable with the given identifier
     * already appears in this context, it will be set visible and focused. Otherwise, the provided supplier will be
     * executed and the resulting dockable will be added to this context.
     * 
     * @param identifier
     *            The identifier in question.
     * @param position
     *            The position in which to display the dockable if it is freshly created.
     * @param supplier
     *            The supplier to use if the dockable is not present.
     */
    public void createOrDisplay(DockableIdentifier identifier, GUIPosition position,
            Supplier<MultipleCDockable> supplier) {
        if (hasDockable(identifier)) {
            MultipleCDockable dockable = getDockable(identifier);
            dockable.setVisible(true);
            if (dockable instanceof DefaultMultipleCDockable) {
                ((DefaultMultipleCDockable) dockable).toFront();
            }
        } else {
            addDockable(supplier.get(), position, identifier);
        }
    }

    /**
     * Introduces the provided {@link MultipleCDockable} to the GUI if its identifier is unique. This method will also
     * set the dockable visible. If the dockable becomes invisible, it is automatically removed from this context.
     * 
     * @param dockable
     *            The dockable to add and display.
     * @param position
     *            The preferred position of the new component.
     * @param identifier
     *            An identifier to use to name the provided dockable. If this identifier is already asssociated with a
     *            dockable in this execution context, the dockable is not added.
     * @return <code>true</code> if the dockable was added; <code>false</code> if it was not.
     */
    public boolean addDockable(MultipleCDockable dockable, GUIPosition position, DockableIdentifier identifier) {
        if (hasDockable(identifier)) {
            return false;
        } else {
            // Actually place the dockable.
            position.place(this.placementContext, dockable);
            CDockableStateListener stateListener = new CDockableStateListener() {
                @Override
                public void visibilityChanged(CDockable dockable) {
                    if (!dockable.isVisible() && dockable instanceof MultipleCDockable) {
                        GUIExecutionContext.this.removeDockable((MultipleCDockable) dockable);
                    }
                }

                @Override
                public void extendedModeChanged(CDockable dockable, ExtendedMode mode) {
                }
            };
            dockable.addCDockableStateListener(stateListener);

            // Update data structures tracking our dockables.
            this.dockableMap.put(identifier, new DockableEntry(dockable, stateListener));

            Set<DockableIdentifier> identifiers = this.reverseDockableMap.get(dockable);
            if (identifiers == null) {
                identifiers = new HashSet<>();
                this.reverseDockableMap.put(dockable, identifiers);
            }
            identifiers.add(identifier);

            // Set the dockable to visible.
            dockable.setVisible(true);
            return true;
        }
    }

    /**
     * Retrieves the dockable associated with the provided {@link DockableIdentifier}.
     * 
     * @param identifier
     *            The identifier in question.
     * @return The dockable or <code>null</code> if no such dockable is registered.
     */
    public MultipleCDockable getDockable(DockableIdentifier identifier) {
        DockableEntry entry = this.dockableMap.get(identifier);
        return entry == null ? null : entry.getDockable();
    }

    /**
     * Determines if the provided {@link DockableIdentifier} is associated with a dockable in this context.
     * 
     * @param identifier
     *            The identifier in question.
     * @return <code>true</code> if a dockable is associated with that identifier; <code>false</code> if none is.
     */
    public boolean hasDockable(DockableIdentifier identifier) {
        return this.dockableMap.containsKey(identifier);
    }

    /**
     * Removes the provided {@link MultipleCDockable} from the GUI.
     * 
     * @param dockable
     *            The dockable to remove.
     */
    public void removeDockable(MultipleCDockable dockable) {
        this.placementContext.getDockingControl().removeDockable(dockable);
        Set<DockableIdentifier> identifiers = this.reverseDockableMap.get(dockable);
        if (identifiers != null) {
            for (DockableIdentifier identifier : identifiers) {
                DockableEntry entry = this.dockableMap.remove(identifier);
                if (entry != null) {
                    dockable.removeCDockableStateListener(entry.getStateListener());
                }
            }
        }
    }

    // This abstraction is in place in anticipation of future complexity.
    private class DockableEntry {
        private MultipleCDockable dockable;
        private CDockableStateListener stateListener;

        public DockableEntry(MultipleCDockable dockable, CDockableStateListener stateListener) {
            super();
            this.dockable = dockable;
            this.stateListener = stateListener;
        }

        public MultipleCDockable getDockable() {
            return dockable;
        }

        public CDockableStateListener getStateListener() {
            return stateListener;
        }
    }
}
