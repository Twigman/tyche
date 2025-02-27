package de.qwyt.housecontrol.tyche.event.sensor;

import org.springframework.context.ApplicationEvent;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SensorEvent extends ApplicationEvent {

	private static final long serialVersionUID = 3698822428553536105L;
	
	private HousecontrolModule module;
	
	private Sensor sensor;
	
	public SensorEvent(Object source, HousecontrolModule module, Sensor sensor) {
		super(source);
		this.module = module;
		this.sensor = sensor;
	}
}
