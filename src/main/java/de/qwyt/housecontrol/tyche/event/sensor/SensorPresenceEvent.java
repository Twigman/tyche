package de.qwyt.housecontrol.tyche.event.sensor;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorPresenceEvent extends SensorEvent {

	private static final long serialVersionUID = 9012706990902344024L;
	
	private PresenceSensor sensor;
	
	public SensorPresenceEvent(Object source, HousecontrolModule module, PresenceSensor sensor) {
		super(source, module);
		this.sensor = sensor;
	}
}
