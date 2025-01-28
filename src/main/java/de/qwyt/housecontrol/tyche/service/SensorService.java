package de.qwyt.housecontrol.tyche.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;

@Service
public class SensorService {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private Map<String, Sensor> sensorMap;
	
	private final ObjectMapper objectMapper;
	
	private final ModelMapper modelMapper;
	
	@Autowired
	public SensorService(ObjectMapper objectMapper, ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.objectMapper = objectMapper;
		this.sensorMap = new HashMap<>();
	}
	
	public boolean registerSensors(String jsonResponse) {
		// parse Sensors
		try {
			// parse JSON-String
		    JsonNode rootNode = objectMapper.readTree(jsonResponse);
		    
		    // iterate over all root nodes
		    for (Iterator<Map.Entry<String, JsonNode>> it = rootNode.fields(); it.hasNext(); ) {
		        Map.Entry<String, JsonNode> entry = it.next();
		        JsonNode sensorNode = entry.getValue();

		        // Extract the sensor type from the root node
		        String sensorType = sensorNode.get("type").asText();
		        sensorNode = this.extendSensorStateByType(sensorNode, sensorType);

		        // Convert JsonNode into a Sensor object
		        Sensor sensor = objectMapper.treeToValue(sensorNode, Sensor.class);

		        // Insert sensor with uniqueId as key into the map
		        if (sensor.getUniqueId() != null) {
		            sensorMap.put(sensor.getUniqueId(), sensor);
		            LOG.debug("New " + sensor.getClass().getSimpleName() + " registered (" + sensor.getUniqueId() + ")");
		        } else {
		            LOG.error("No unique ID found for sensor " + sensor.getName() + " (" + sensor.getManufacturer() + ")");
		        }
		    }
		
			LOG.info(this.sensorMap.size() + " sensors registered");
		} catch (JsonProcessingException e) {
			LOG.error("Error during Sensor registration");
			e.printStackTrace();
		}
		
		return false;
		
	}


	public void updateSensorByJson(String uniqueId, JsonNode rootMessage) {
		Sensor sensor = this.sensorMap.get(uniqueId);
		
		if (rootMessage.has("state")) {
			// state event
			this.updateSensorStateByJson(sensor, rootMessage);
		} else if (rootMessage.has("attr")) {
			// attr event
			this.updateSensorAttrByJson(sensor, rootMessage);
		} else {
			LOG.warn("Event doesn't contains a state- or attr-Attribute: {}", rootMessage.toString());
		}
	}
	
	private JsonNode extendSensorStateByType(JsonNode root, String sensorType) {
		JsonNode stateNode = root.get("state");
		
		// Add type in stateNode if it is an ObjectNode
		if (stateNode != null && stateNode.isObject()) {
			((ObjectNode) stateNode).put("type", sensorType);
		    ((ObjectNode) root).set("state", stateNode);
		} else {
			LOG.warn("Sensor has no 'state' attribute.");
		}
		return root;
	}
	
	
	private void updateSensorAttrByJson(Sensor sensor, JsonNode root) {
		// Convert JsonNode into a Sensor object
		JsonNode attrNode = root.get("attr");
		
        try {
        	LOG.debug(sensor.toString());
			Sensor changedSensor = objectMapper.treeToValue(attrNode, Sensor.class);
			this.modelMapper.map(changedSensor, sensor);
			
			LOG.debug("Sensor {} updated", sensor.getUniqueId());
		} catch (JsonProcessingException | IllegalArgumentException e) {
			LOG.error("Can't create Sensor from JSON: {}", root.toString());
			e.printStackTrace();
		}
	}
	

	public void updateSensorStateByJson(Sensor sensor, JsonNode root) {
		
		root = this.extendSensorStateByType(root, sensor.getType());
		// Map state-entry to concrete SensorState and change values of the registered sensor
		JsonNode stateNode = root.get("state");

		try {
		    // Deserialize stateNode to a concrete SensorState instance
		    SensorState changedSensorState = this.objectMapper.treeToValue(stateNode, sensor.getState().getClass());
		    
		    // Update sensor state only for non-null attributes
		    this.modelMapper.map(changedSensorState, sensor.getState());
		    LOG.debug("Sensor {} updated", sensor.getUniqueId());
		} catch (JsonProcessingException | IllegalArgumentException e) {
		    LOG.error("Can't create SensorState from JSON: {}", stateNode.toString());
		    e.printStackTrace();
		}
	}
	
	
	public void addSensor(Sensor sensor) {
		this.sensorMap.put(sensor.getId(), sensor);
	}

}
