package com.bahj.smelt.plugin.builtin.basegui.execution;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CWorkingArea;

public class PlacementContext {
    private CControl dockingControl;
    private CWorkingArea primaryWorkingArea;

    public PlacementContext(CControl dockingControl, CWorkingArea primaryWorkingArea) {
        super();
        this.dockingControl = dockingControl;
        this.primaryWorkingArea = primaryWorkingArea;
    }

    public CControl getDockingControl() {
        return dockingControl;
    }

    public CWorkingArea getPrimaryWorkingArea() {
        return primaryWorkingArea;
    }

}
