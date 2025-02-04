package de.qwyt.housecontrol.tyche.event.sensor;

import org.springframework.context.ApplicationEvent;

import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import lombok.Getter;

@Getter
public class SensorPresenceEvent extends ApplicationEvent {

	private PresenceSensor sensor;
	
	public SensorPresenceEvent(Object source, PresenceSensor sensor) {
		super(source);
		this.sensor = sensor;
	}
}
