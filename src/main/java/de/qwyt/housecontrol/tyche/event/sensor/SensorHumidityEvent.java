package de.qwyt.housecontrol.tyche.event.sensor;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.sensor.zha.HumiditySensor;

public class SensorHumidityEvent extends SensorEvent {

	private static final long serialVersionUID = -6842694794851678957L;
	
	
	public SensorHumidityEvent(Object source, HousecontrolModule module, HumiditySensor sensor) {
		super(source, module, sensor);
	}

	@Override
	public HumiditySensor getSensor() {
		return (HumiditySensor) super.getSensor();
	}
}
