package com.bahj.smelt.plugin.builtin.basegui.execution;

import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.MultipleCDockable;

/**
 * An enumeration of basic, static positions in the GUI.
 * 
 * @author Zachary Palmer
 */
public enum BasicGUIPosition implements GUIPosition {
    /** Indicates the primary working area. */
    PRIMARY,
    /** Indicates the left side of the dockable space. */
    LEFT,
    /** Indicates the right side of the dockable space. */
    RIGHT,
    /** Indicates the top side of the dockable space. */
    TOP,
    /** Indicates the bottom side of the dockable space. */
    BOTTOM;

    @Override
    public void place(PlacementContext context, MultipleCDockable dockable) {
        if (this == PRIMARY) {
            context.getPrimaryWorkingArea().add(dockable);
        } else {
            context.getDockingControl().addDockable(dockable);
            final double borderSize = 0.25;
            CLocation location = null;
            switch (this) {
                case LEFT:
                    location = CLocation.base(context.getDockingControl().getContentArea()).normalWest(borderSize);
                    break;
                case RIGHT:
                    location = CLocation.base(context.getDockingControl().getContentArea()).normalEast(borderSize);
                    break;
                case TOP:
                    location = CLocation.base(context.getDockingControl().getContentArea()).normalNorth(borderSize);
                    break;
                case BOTTOM:
                    location = CLocation.base(context.getDockingControl().getContentArea()).normalSouth(borderSize);
                    break;
                default:
                    throw new IllegalStateException("Unexpected enum value: " + String.valueOf(this));
            }
            dockable.setLocation(location);
        }
    }
}
