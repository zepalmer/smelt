package com.bahj.smelt.plugin.builtin.data.model.value.event;

import com.bahj.smelt.plugin.builtin.data.model.value.SmeltDatum;

/**
 * A special {@link SmeltDatumEvent} which is fired when the title property changes. This is relevant because the title
 * property is used to display the object in some places. This event is fired after the general update event for the
 * property.
 * 
 * @author Zachary Palmer
 */
public class SmeltDatumTitlePropertyUpdateEvent extends SmeltDatumEvent implements
        SmeltValueDescriptionUpdateEvent<SmeltDatum, SmeltDatumEvent> {
    public SmeltDatumTitlePropertyUpdateEvent(SmeltDatum value) {
        super(value);
    }
}
