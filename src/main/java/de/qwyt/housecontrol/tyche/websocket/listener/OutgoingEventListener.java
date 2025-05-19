package de.qwyt.housecontrol.tyche.websocket.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import de.qwyt.housecontrol.tyche.event.AutomationActiveProfileEvent;
import de.qwyt.housecontrol.tyche.event.LogEvent;
import de.qwyt.housecontrol.tyche.event.PhoneInfoEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorHumidityEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorTemperatureEvent;
import de.qwyt.housecontrol.tyche.websocket.adapter.StompMessageAdapter;

@Component
public class OutgoingEventListener {
	
	private final StompMessageAdapter stompMessageAdapter;
	
	@Autowired
	public OutgoingEventListener(StompMessageAdapter stompMessageAdapter) {
		this.stompMessageAdapter = stompMessageAdapter;
	}
	
	@Async
	@EventListener
	public void onSensorPresenceEvent(SensorPresenceEvent event) {
		stompMessageAdapter.sendSensorPresenceUpdate(event);
	}
	
	@Async
	@EventListener
	public void onSensorTemperatureEvent(SensorTemperatureEvent event) {
		stompMessageAdapter.sendSensorTemperatureUpdate(event);
	}

	@Async
	@EventListener
	public void onSensorHumidityEvent(SensorHumidityEvent event) {
		stompMessageAdapter.sendSensorHumidityUpdate(event);
	}
	
	@Async
	@EventListener
	public void onPhoneInfoEvent(PhoneInfoEvent event) {
		stompMessageAdapter.sendPhoneInfoUpdate(event);
	}
	
	@Async
	@EventListener
	public void onAutomationActiveProfileEvent(AutomationActiveProfileEvent event) {
		stompMessageAdapter.sendAutomationActiveProfileUpdate(event);
	}
	
	@Async
	@EventListener
	public void onLogEvent(LogEvent event) {
		stompMessageAdapter.sendLogEvent(event);
	}
}
