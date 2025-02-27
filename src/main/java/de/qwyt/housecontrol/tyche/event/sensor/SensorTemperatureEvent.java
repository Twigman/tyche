package de.qwyt.housecontrol.tyche.event.sensor;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.sensor.zha.TemperatureSensor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorTemperatureEvent extends SensorEvent {

	private static final long serialVersionUID = 7393717207748209350L;
	
	private TemperatureSensor sensor;
	
	public SensorTemperatureEvent(Object source, HousecontrolModule module, TemperatureSensor sensor) {
		super(source, module);
		this.sensor = sensor;
	}
}
