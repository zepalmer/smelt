package com.bahj.smelt.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bahj.smelt.plugin.SmeltPlugin;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationException;
import com.bahj.smelt.serialization.SmeltStringSerializationStrategy;

/**
 * Describes a Smelt configuration which dictates how models are constructed. A Smelt configuration indicates which
 * plugins are present and instantiates the plugin registry, which is ultimately responsible for defining the semantics
 * of the data model.
 * 
 * @author Zachary Palmer
 */
public class Configuration {
    // @formatter:off
    public static final String[] DEFAULT_PLUGIN_NAMES = new String[] {
        "com.bahj.smelt.plugin.builtin.data.DataPlugin",
        "com.bahj.smelt.plugin.builtin.basegui.BaseGUIPlugin",
        "com.bahj.smelt.plugin.builtin.editor.EditorPlugin",
        "com.bahj.smelt.plugin.builtin.viewbytype.ViewByTypePlugin"
    };
    // @formatter:on

    private Set<Class<? extends SmeltPlugin>> plugins;

    /**
     * Creates a new, empty configuration.
     */
    public Configuration() {
        this.plugins = new HashSet<>();
    }

    public void addPlugin(Class<? extends SmeltPlugin> clazz) {
        this.plugins.add(clazz);
    }

    public void removePlugin(Class<? extends SmeltPlugin> clazz) {
        this.plugins.remove(clazz);
    }

    public void clearPlugins() {
        this.plugins.clear();
    }

    public Set<Class<? extends SmeltPlugin>> getPlugins() {
        return this.plugins;
    }
    
    /**
     * Creates a configuration which contains the default plugins.
     * @return A new configuration with default plugins.
     * @throws ClassNotFoundException If one of the default classes cannot be found.
     * @throws ClassCastException If one of the default classes cannot be coerced to {@link SmeltPlugin}.
     */
    public static Configuration createDefaultConfiguration() throws ClassNotFoundException, ClassCastException {
        Configuration configuration = new Configuration();
        for (String name : DEFAULT_PLUGIN_NAMES) {
            Class<?> clazz = Class.forName(name);
            Class<? extends SmeltPlugin> pluginClass = clazz.asSubclass(SmeltPlugin.class);
            configuration.addPlugin(pluginClass);
        }
        return configuration;
    }

    public static class SerializationStrategy implements SmeltStringSerializationStrategy<Configuration> {
        public static final SerializationStrategy INSTANCE = new SerializationStrategy();

        private SerializationStrategy() {
        }

        @Override
        public String objectToString(Configuration obj) throws SerializationException {
            StringBuilder builder = new StringBuilder();
            List<Class<? extends SmeltPlugin>> plugins = new ArrayList<>(obj.getPlugins());
            Collections.sort(plugins, (Class<? extends SmeltPlugin> c1, Class<? extends SmeltPlugin> c2) -> c1
                    .getName().compareTo(c2.getName()));
            for (Class<? extends SmeltPlugin> clazz : plugins) {
                if (builder.length() > 0) {
                    builder.append(',');
                }
                builder.append(clazz.getName());
            }
            return builder.toString();
        }

        @Override
        public Configuration stringToObject(String str) throws DeserializationException {
            Configuration configuration = new Configuration();
            for (String className : str.trim().split(",")) {
                Class<?> clazz;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new DeserializationException(e);
                }
                Class<? extends SmeltPlugin> pluginClass;
                try {
                    pluginClass = clazz.asSubclass(SmeltPlugin.class);
                } catch (ClassCastException e) {
                    throw new DeserializationException("Class " + clazz.getName() + " is not a smelt plugin.");
                }
                configuration.addPlugin(pluginClass);
            }
            return configuration;
        }
    }
}
