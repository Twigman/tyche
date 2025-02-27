package de.qwyt.housecontrol.tyche.service.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.event.sensor.DimmerSwitchEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorHumidityEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorTemperatureEvent;

@Service
public class StompMessageService {
	
	private final SimpMessagingTemplate messagingTemplate;
	
	@Value("${tyche.stomp.topic.sensor.presence}")
	private String sensorPresenceTopic;
	
	@Value("${tyche.stomp.topic.sensor.temperature}")
	private String sensorTemperatureTopic;
	
	@Value("${tyche.stomp.topic.sensor.pressure}")
	private String sensorPressureTopic;
	
	@Value("${tyche.stomp.topic.sensor.lightlevel}")
	private String sensorLightLevelTopic;
	
	@Value("${tyche.stomp.topic.sensor.humidity}")
	private String sensorHumidityTopic;
	
	@Value("${tyche.stomp.topic.sensor.dimmerswitch}")
	private String sensorDimmerSwitchTopic;
	
	@Value("${tyche.stomp.topic.light.huelight}")
	private String hueLightTopic;
	
	@Autowired
	public StompMessageService(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	public void sendSensorPresenceUpdate(SensorPresenceEvent event) {
		messagingTemplate.convertAndSend(sensorPresenceTopic, event);
	}
	
	public void sendSensorDimmerSwitchUpdate(DimmerSwitchEvent event) {
		messagingTemplate.convertAndSend(sensorPresenceTopic, event);
	}

	public void sendSensorTemperatureUpdate(SensorTemperatureEvent event) {
		messagingTemplate.convertAndSend(sensorTemperatureTopic, event);
	}
	
	public void sendSensorHumidityUpdate(SensorHumidityEvent event) {
		messagingTemplate.convertAndSend(sensorHumidityTopic, event);
	}
	
}
