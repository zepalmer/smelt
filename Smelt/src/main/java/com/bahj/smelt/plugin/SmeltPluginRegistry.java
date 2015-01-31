package com.bahj.smelt.plugin;

import java.util.Map;

/**
 * The interface for Smelt plugin registries.
 * @author Zachary Palmer
 */
public interface SmeltPluginRegistry {
    /**
     * Retrieves the Smelt plugin of the provided class which has been connected to this registry.
     * @param pluginClass The class of plugin to retrieve.
     * @return The plugin, or <code>null</code> if no plugin of that class exists.
     */
    public <T extends SmeltPlugin> T getPlugin(Class<T> pluginClass);

    public Map<Class<? extends SmeltPlugin>, SmeltPlugin> asMap();
}