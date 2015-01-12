package com.bahj.smelt.model.datamodel.event;

import com.bahj.smelt.model.datamodel.SmeltDataModel;

public class SmeltDataModelInitializedEvent extends SmeltDataModelEvent {
    public SmeltDataModelInitializedEvent(SmeltDataModel dataModel) {
        super(dataModel);
    }
}
