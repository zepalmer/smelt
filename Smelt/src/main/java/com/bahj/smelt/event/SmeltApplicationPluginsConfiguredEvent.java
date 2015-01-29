package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;

/**
 * This event is fired immediately after plugins are configured and registered with the application data model. Plugins
 * should react to this event by initializing the state of the application (e.g. opening GUI windows).
 * <p/>
 * This event is fired immediately after {@link SmeltApplicationConfigurationLoadedEvent}.
 * 
 * @author Zachary Palmer
 */
public class SmeltApplicationPluginsConfiguredEvent extends SmeltApplicationEvent {
    public SmeltApplicationPluginsConfiguredEvent(SmeltApplicationModel model) {
        super(model);
    }
}
