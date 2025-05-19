package de.qwyt.housecontrol.tyche.websocket.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import de.qwyt.housecontrol.tyche.event.AutomationActiveProfileEvent;
import de.qwyt.housecontrol.tyche.event.LogEvent;
import de.qwyt.housecontrol.tyche.event.PhoneInfoEvent;
import de.qwyt.housecontrol.tyche.event.sensor.DimmerSwitchEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorHumidityEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorTemperatureEvent;

@Component
public class StompMessageAdapter {
	
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
	
	@Value("${tyche.stomp.topic.phone.info}")
	private String phoneInfoTopic;
	
	@Value("${tyche.stomp.topic.sensor.dimmerswitch}")
	private String sensorDimmerSwitchTopic;
	
	@Value("${tyche.stomp.topic.light.huelight}")
	private String hueLightTopic;
	
	@Value("${tyche.stomp.topic.automation.active-profile}")
	private String automationActiveProfileTopic;
	
	@Value("${tyche.stomp.topic.app.log}")
	private String appLogTopic;
	
	@Autowired
	public StompMessageAdapter(SimpMessagingTemplate messagingTemplate) {
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

	public void sendPhoneInfoUpdate(PhoneInfoEvent event) {
		messagingTemplate.convertAndSend(phoneInfoTopic, event);
	}

	public void sendAutomationActiveProfileUpdate(AutomationActiveProfileEvent event) {
		messagingTemplate.convertAndSend(automationActiveProfileTopic, event);
	}

	public void sendLogEvent(LogEvent event) {
		messagingTemplate.convertAndSend(appLogTopic, event);
	}
	
}
