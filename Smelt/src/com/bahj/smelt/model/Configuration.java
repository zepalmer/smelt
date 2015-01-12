package com.bahj.smelt.model;

import java.util.ArrayList;
import java.util.Arrays;
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
    public static final Set<Class<? extends SmeltPlugin>> DEFAULT_PLUGINS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList()));

    private Set<Class<? extends SmeltPlugin>> plugins;

    public Configuration() {
        this.plugins = new HashSet<>(DEFAULT_PLUGINS);
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
        return this.getPlugins();
    }

    public static class SerializationStrategy implements SmeltStringSerializationStrategy<Configuration> {
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
