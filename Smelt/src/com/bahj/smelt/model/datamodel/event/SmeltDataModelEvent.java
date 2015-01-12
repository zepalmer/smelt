package com.bahj.smelt.model.datamodel.event;

import com.bahj.smelt.model.datamodel.SmeltDataModel;
import com.bahj.smelt.util.event.Event;

public class SmeltDataModelEvent implements Event {
    private SmeltDataModel dataModel;

    public SmeltDataModelEvent(SmeltDataModel dataModel) {
        super();
        this.dataModel = dataModel;
    }

    public SmeltDataModel getDataModel() {
        return dataModel;
    }

}
