package de.qwyt.housecontrol.tyche.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
		        
		        // Check if the "state" object exists and is a JSON object
		        JsonNode stateNode = sensorNode.get("state");
		        if (stateNode != null && stateNode.isObject()) {
		            // Insert the "type" attribute into the "state" object
		            ((ObjectNode) stateNode).put("type", sensorType);
		        }
		        

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
			// TODO Auto-generated catch block
			LOG.error("Error during Sensor registration");
			e.printStackTrace();
		}
		
		return false;
		
	}


	public void updateSensorByJson(String uniqueId, JsonNode root) {
		Sensor sensor = this.sensorMap.get(uniqueId);
		
		if (root.has("state")) {
			// state event
			
			// Map state-entry to concrete SensorState and change values of the registered sensor
			JsonNode stateNode = root.get("state");
			String sensorType = sensor.getType();

			// Add type in stateNode if it is an ObjectNode
			if (stateNode != null && stateNode.isObject()) {
			    ((ObjectNode) stateNode).put("type", sensorType);
			}

			LOG.debug(sensor.getState().toString());

			try {
			    // Deserialize stateNode to a concrete SensorState instance
			    SensorState changedSensorState = this.objectMapper.treeToValue(stateNode, sensor.getState().getClass());
			    
			    // Update sensor state only for non-null attributes
			    this.updateSensorStateForNotNullAttributes(changedSensorState, sensor.getState());
			    LOG.debug("Sensor " + uniqueId + " updated");
			} catch (JsonProcessingException | IllegalArgumentException e) {
			    LOG.error("Can't create SensorState from JSON: " + stateNode.toString());
			    e.printStackTrace();
			}
			
		} else if (root.has("attr")) {
			// attr event
		} else {
			LOG.warn("Event doesn't contains a state- or attr-Attribute: {}", root.toString());
		}
	}
	
	public void updateSensorStateForNotNullAttributes(SensorState sourceSensorState, SensorState targetSensorState) {
		modelMapper.map(sourceSensorState, targetSensorState);
	}
	
	public void updateSensor(Sensor sourceSensor, Sensor targetSensor) {
		modelMapper.map(sourceSensor, targetSensor);
	}
	
	public void addSensor(Sensor sensor) {
		this.sensorMap.put(sensor.getId(), sensor);
	}

}
