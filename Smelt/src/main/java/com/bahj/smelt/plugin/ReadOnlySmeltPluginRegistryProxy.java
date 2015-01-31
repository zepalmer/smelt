package com.bahj.smelt.plugin;

import java.util.Collections;
import java.util.Map;

public class ReadOnlySmeltPluginRegistryProxy implements SmeltPluginRegistry {
    private SmeltPluginRegistry registry;

    public ReadOnlySmeltPluginRegistryProxy(SmeltPluginRegistry registry) {
        super();
        this.registry = registry;
    }

    @Override
    public <T extends SmeltPlugin> T getPlugin(Class<T> pluginClass) {
        return this.registry.getPlugin(pluginClass);
    }

    @Override
    public Map<Class<? extends SmeltPlugin>, SmeltPlugin> asMap() {
        return Collections.unmodifiableMap(this.registry.asMap());
    }
}
