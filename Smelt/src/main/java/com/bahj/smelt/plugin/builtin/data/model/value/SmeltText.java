package com.bahj.smelt.plugin.builtin.data.model.value;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltTextEvent;
import com.bahj.smelt.plugin.builtin.data.model.value.event.SmeltTextUpdateEvent;

public class SmeltText extends AbstractSmeltValue<SmeltText, SmeltTextEvent> {
	private String value;

	public SmeltText(SmeltType<SmeltText> type, String value) {
        super(type);
        this.value = value;
    }

    public String getValue() {
		return value;
	}

    public void setValue(String value) {
        if (!value.equals(this.value)) {
            String oldValue = this.value;
            this.value = value;
            fireEvent(new SmeltTextUpdateEvent(this, oldValue, this.value));
        }
    }
}
