package de.qwyt.housecontrol.tyche.service;

import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.event.AutomationActiveProfileEvent;
import de.qwyt.housecontrol.tyche.event.LogEvent;
import de.qwyt.housecontrol.tyche.event.PhoneInfoEvent;
import de.qwyt.housecontrol.tyche.event.RoomVisitThresholdReachedEvent;
import de.qwyt.housecontrol.tyche.event.sensor.DimmerSwitchEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorHumidityEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorTemperatureEvent;
import de.qwyt.housecontrol.tyche.event.types.HousecontrolModule;
import de.qwyt.housecontrol.tyche.event.types.LogLevel;
import de.qwyt.housecontrol.tyche.model.device.PhoneInfo;
import de.qwyt.housecontrol.tyche.model.group.RoomType;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfileType;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import de.qwyt.housecontrol.tyche.model.sensor.zha.HumiditySensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.TemperatureSensor;

@Service
public class EventServiceImpl {

	private final ApplicationEventPublisher eventPublisher;
	
	private final Map<Class<? extends Sensor>, Function<Sensor, ApplicationEvent>> eventMappings;
	
	@Autowired
	public EventServiceImpl(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
		this.eventMappings = Map.of(
				PresenceSensor.class, sensor -> new SensorPresenceEvent(this, HousecontrolModule.ZIGBEE, (PresenceSensor) sensor),
				DimmerSwitch.class, sensor -> new DimmerSwitchEvent(this, HousecontrolModule.ZIGBEE, (DimmerSwitch) sensor),
				TemperatureSensor.class, sensor -> new SensorTemperatureEvent(this, HousecontrolModule.ZIGBEE, (TemperatureSensor) sensor),
				HumiditySensor.class, sensor -> new SensorHumidityEvent(this, HousecontrolModule.ZIGBEE, (HumiditySensor) sensor)
				);
	}
	
	public void fireSensorEvent(Sensor sensor) {
		Function<Sensor, ApplicationEvent> eventCreator = eventMappings.get(sensor.getClass());
		
		if (eventCreator != null) {
			eventPublisher.publishEvent(eventCreator.apply(sensor));
		}
	}
	
	public void fireRoomVisitThresholdReachedEvent(RoomType type) {
		eventPublisher.publishEvent(new RoomVisitThresholdReachedEvent(this, HousecontrolModule.SYSTEM, type));
	}
	
	public void firePhoneEvent(PhoneInfo info) {
		eventPublisher.publishEvent(new PhoneInfoEvent(this, HousecontrolModule.FRITZBOX, info));
	}
	
	public void fireAutomationActiveProfile(HousecontrolModule module, AutomationProfileType activeProfile) {
		eventPublisher.publishEvent(new AutomationActiveProfileEvent(this, module, activeProfile));
	}
	
	public void fireLogEvent(HousecontrolModule module, LogLevel level, String message) {
		eventPublisher.publishEvent(new LogEvent(this, module, level, message));
	}
	
}
