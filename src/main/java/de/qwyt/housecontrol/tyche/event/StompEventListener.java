package de.qwyt.housecontrol.tyche.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorTemperatureEvent;
import de.qwyt.housecontrol.tyche.service.websocket.StompMessageService;

@Component
public class StompEventListener {
	
	private final StompMessageService stompMessageService;
	
	@Autowired
	public StompEventListener(StompMessageService stompMessageService) {
		this.stompMessageService = stompMessageService;
	}
	
	@Async
	@EventListener
	public void onSensorPresenceEvent(SensorPresenceEvent event) {
		stompMessageService.sendSensorPresenceUpdate(event);
	}
	
	@Async
	@EventListener
	public void onSensorTemperatureEvent(SensorTemperatureEvent event) {
		stompMessageService.sendSensorTemperatureUpdate(event);
	}

}
