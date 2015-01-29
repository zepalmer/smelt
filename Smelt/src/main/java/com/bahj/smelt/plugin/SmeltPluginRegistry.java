package com.bahj.smelt.plugin;

import java.util.Map;

import com.bahj.smelt.util.TypeDependentMap;

/**
 * A registry for Smelt plugins.  The Smelt plugin registry holds a single instance of each loaded plugin.
 * @author Zachary Palmer
 */
public class SmeltPluginRegistry {
    private TypeDependentMap<SmeltPlugin> map;

    public SmeltPluginRegistry() {
        this.map = new TypeDependentMap<>();
    }

    public <T extends SmeltPlugin> void registerPlugin(Class<T> pluginClass, T plugin) {
        this.map.put(pluginClass, plugin);
    }
    
    public <T extends SmeltPlugin> T getPlugin(Class<T> pluginClass) {
        return this.map.get(pluginClass);
    }
    
    public Map<Class<? extends SmeltPlugin>,SmeltPlugin> asMap() {
        return this.map.asMap();
    }
}
