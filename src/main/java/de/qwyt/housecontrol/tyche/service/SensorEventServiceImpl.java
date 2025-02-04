package de.qwyt.housecontrol.tyche.service;

import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.event.sensor.DimmerSwitchEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;

@Service
public class SensorEventServiceImpl {

	private final ApplicationEventPublisher eventPublisher;
	
	private final Map<Class<? extends Sensor>, Function<Sensor, ApplicationEvent>> eventMappings;
	
	@Autowired
	public SensorEventServiceImpl(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
		this.eventMappings = Map.of(
				PresenceSensor.class, sensor -> new SensorPresenceEvent(this, (PresenceSensor) sensor),
				DimmerSwitch.class, sensor -> new DimmerSwitchEvent(this, (DimmerSwitch) sensor)
				);
	}
	
	public void processSensorChange(Sensor sensor) {
		Function<Sensor, ApplicationEvent> eventCreator = eventMappings.get(sensor.getClass());
		
		if (eventCreator != null) {
			eventPublisher.publishEvent(eventCreator.apply(sensor));
		}
	}
	
}
