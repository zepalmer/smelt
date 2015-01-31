package com.bahj.smelt.plugin.builtin.data.model.value;

import com.bahj.smelt.plugin.builtin.data.model.type.SmeltType;

public class SmeltEnumValue extends AbstractSmeltValue<SmeltEnumValue> {
	private String choice;

	public SmeltEnumValue(SmeltType<SmeltEnumValue> type, String choice) {
        super(type);
        this.choice = choice;
    }

    public String getChoice() {
		return choice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((choice == null) ? 0 : choice.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmeltEnumValue other = (SmeltEnumValue) obj;
		if (choice == null) {
			if (other.choice != null)
				return false;
		} else if (!choice.equals(other.choice))
			return false;
		return true;
	}
}
