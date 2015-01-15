package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.plugin.SmeltPlugin;

public abstract class SmeltPluginEvent extends SmeltApplicationEvent {
    private SmeltPlugin plugin;

    public SmeltPluginEvent(SmeltApplicationModel applicationModel, SmeltPlugin plugin) {
        super(applicationModel);
        this.plugin = plugin;
    }

    public SmeltPlugin getPlugin() {
        return plugin;
    }
}
