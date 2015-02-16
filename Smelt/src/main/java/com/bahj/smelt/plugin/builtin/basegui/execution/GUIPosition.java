package com.bahj.smelt.plugin.builtin.basegui.execution;

import bibliothek.gui.dock.common.MultipleCDockable;

/**
 * An interface describing the positions in the GUI where dockables can be added.
 * 
 * @author Zachary Palmer
 */
public interface GUIPosition {
    /**
     * Asks this GUI position to place the provided dockable. This method must not modify the environment in any way
     * except to add the provided dockable to the docking contents somewhere.
     * 
     * @param context
     *            The placement context in which to place the dockable.
     * @param dockable
     *            The dockable to place.
     */
    public void place(PlacementContext context, MultipleCDockable dockable);
}
