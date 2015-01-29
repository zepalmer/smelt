package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.configuration.Configuration;

/**
 * This event is fired immediately after the configuration is loaded into a Smelt application model. This is the first
 * event which is fired in the application. After this event is fired, all plugins will have been registered. Plugins
 * should react to this event by adding listeners to the plugins on which they depend.
 * <p/>
 * After this event is fired, the next step taken by the application is to fire a
 * {@link SmeltApplicationPluginsConfiguredEvent}.
 * 
 * @author Zachary Palmer
 */
public class SmeltApplicationConfigurationLoadedEvent extends SmeltApplicationEvent {
    private Configuration configuration;

    public SmeltApplicationConfigurationLoadedEvent(SmeltApplicationModel model, Configuration configuration) {
        super(model);
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
