package com.bahj.smelt.event;

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.model.Configuration;

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
