package com.bahj.smelt.plugin;

import java.util.Map;

import com.bahj.smelt.util.TypeDependentMap;

/**
 * A registry implementation for Smelt plugins. The Smelt plugin registry holds a single instance of each loaded plugin.
 * 
 * @author Zachary Palmer
 */
public class SmeltPluginRegistryImpl implements SmeltPluginRegistry {
    private TypeDependentMap<SmeltPlugin> map;

    public SmeltPluginRegistryImpl() {
        this.map = new TypeDependentMap<>();
    }

    public <T extends SmeltPlugin> void registerPlugin(Class<T> pluginClass, T plugin) {
        this.map.put(pluginClass, plugin);
    }

    @Override
    public <T extends SmeltPlugin> T getPlugin(Class<T> pluginClass) {
        return this.map.get(pluginClass);
    }

    @Override
    public Map<Class<? extends SmeltPlugin>, SmeltPlugin> asMap() {
        return this.map.asMap();
    }
}
