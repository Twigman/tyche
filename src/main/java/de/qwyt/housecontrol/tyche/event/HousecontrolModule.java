package de.qwyt.housecontrol.tyche.event;

public enum HousecontrolModule {
	ZIGBEE;
	
	public String formatForLog() {
		return String.format("[%12s]", this.name());
	}
}
