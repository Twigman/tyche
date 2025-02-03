package de.qwyt.housecontrol.tyche.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.qwyt.housecontrol.tyche.service.LightService;
import de.qwyt.housecontrol.tyche.service.SensorService;

@Component
public class WebSocketEventListener {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private LightService lightService;
	
	@Autowired
	private ObjectMapper objectMapper;
	

	@EventListener
	public void handleWebSocketMessage(RaspbeeWebSocketEvent webSocketEvent) {
		
		try {
			JsonNode rootMessage = this.objectMapper.readTree(webSocketEvent.getMessage());
			// contains the source (sensors/lights)
			String resourceType = rootMessage.get("r").asText();
			// event
			String eventType = rootMessage.get("e").asText();
			
			
			if (eventType.equals("changed")) {
				if (resourceType.equals("sensors")) {
					// identify the device
					String uniqueId = rootMessage.get("uniqueid").asText();
					this.sensorService.updateSensorByJson(uniqueId, rootMessage);
				} else if (resourceType.equals("lights")) {
					// identify the device
					String uniqueId = rootMessage.get("uniqueid").asText();
					this.lightService.updateLightByJson(uniqueId, rootMessage);
				} else if (resourceType.equals("groups")) {
					// no uniqueId
					LOG.warn("No handling for resource '" + resourceType + "' implemented");
				} else {
					LOG.warn("No handling for resource '" + resourceType + "' implemented");
				}
			} else {
				LOG.warn("No handling for event type '" + eventType + "' implemented");
			}
			
		} catch (JsonProcessingException e) {
			LOG.error("Something went wrong parsing the following RaspbeeWebSocketEvent: " + webSocketEvent.getMessage());
			
			e.printStackTrace();
		}
		
		
		/*
		if (eventType.getMessage().getEvent().equals("changed")) {
			// change
			if (eventType.getMessage().getResourceType().equals("sensors")) {
				// sensor event
				
				//sensorController.updateSensor(event.getMessage().getUniqueId(), event.getMessage().getState());
				
			} else {
				LOG.warn("No handling for resource " + eventType.getMessage().getResourceType() + " implemented");
			}
		} else {
			LOG.warn("No handling for event type '" + eventType.getMessage().getEvent() + "' implemented");
		}
		*/
	}
}
