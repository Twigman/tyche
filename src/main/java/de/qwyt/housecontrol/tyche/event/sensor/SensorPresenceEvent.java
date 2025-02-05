package de.qwyt.housecontrol.tyche.event.sensor;

import org.springframework.context.ApplicationEvent;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import lombok.Getter;

@Getter
public class SensorPresenceEvent extends ApplicationEvent {

	private PresenceSensor sensor;
	
	private HousecontrolModule module;
	
	public SensorPresenceEvent(Object source, HousecontrolModule module, PresenceSensor sensor) {
		super(source);
		this.sensor = sensor;
		this.module = module;
	}
}
