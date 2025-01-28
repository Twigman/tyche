package de.qwyt.housecontrol.tyche.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.qwyt.housecontrol.tyche.model.event.RaspbeeWebSocketEvent;
import de.qwyt.housecontrol.tyche.service.LightService;
import de.qwyt.housecontrol.tyche.service.SensorService;

@Controller
public class RaspbeeWebSocketController {
	
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
			JsonNode root = this.objectMapper.readTree(webSocketEvent.getMessage());
			// contains the source (sensors/lights)
			String resourceType = root.get("r").asText();
			// event
			String eventType = root.get("e").asText();
			// identify the device
			String uniqueId = root.get("uniqueid").asText();
			
			if (eventType.equals("changed")) {
				if (resourceType.equals("sensors")) {
					this.sensorService.updateSensorByJson(uniqueId, root);
				} else if (resourceType.equals("lights")) {
					
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
