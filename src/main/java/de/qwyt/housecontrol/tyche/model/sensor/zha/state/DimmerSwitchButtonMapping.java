package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import lombok.Getter;

@Getter
public enum DimmerSwitchButtonMapping {
	// on
	BUTTON_ON_PRESSED(1000),
	BUTTON_ON_HOLD(1001),
	BUTTON_ON_RELEASED(1002),
	BUTTON_ON_HOLD_RELEASED(1003),
	// brighter
	BUTTON_UP_PRESSED(2000),
	BUTTON_UP_HOLD(2001),
	BUTTON_UP_RELEASED(2002),
	BUTTON_UP_HOLD_RELEASED(2003),
	// dimmer
	BUTTON_DOWN_PRESSED(3000),
	BUTTON_DOWN_HOLD(3001),
	BUTTON_DOWN_RELEASED(3002),
	BUTTON_DOWN_HOLD_RELEASED(3003),
	// of
	BUTTON_OFF_PRESSED(4000),
	BUTTON_OFF_HOLD(4001),
	BUTTON_OFF_RELEASED(4002),
	BUTTON_OFF_HOLD_RELEASED(4003);
	
	private final int code;
	
	private DimmerSwitchButtonMapping(int code) {
		this.code = code;
	}
	
	public static DimmerSwitchButtonMapping fromCode(int code) {
		for (DimmerSwitchButtonMapping mapping : values()) {
			if (mapping.code == code) {
				return mapping;
			}
		}
		throw new IllegalArgumentException("Unknown ButtonEvent Code: " + code);
	}
}
