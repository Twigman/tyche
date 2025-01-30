package de.qwyt.housecontrol.tyche.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import de.qwyt.housecontrol.tyche.repository.sensor.SensorRepository;
import de.qwyt.housecontrol.tyche.repository.sensor.SensorStateRepository;

@Service
public class SensorService {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	// uniqueId is the Key
	private Map<String, Sensor> sensorMap;
	
	private final ObjectMapper objectMapper;
	
	private final ModelMapper modelMapper;
	
	private final SensorRepository sensorRepository;
	
	private final SensorStateRepository sensorStateRepository;
	
	@Autowired
	public SensorService(
			ObjectMapper objectMapper,
			ModelMapper modelMapper,
			SensorRepository sensorRepository,
			SensorStateRepository sensorStateRepository
			) {
		this.modelMapper = modelMapper;
		this.objectMapper = objectMapper;
		this.sensorRepository = sensorRepository;
		this.sensorStateRepository = sensorStateRepository;
		this.sensorMap = new HashMap<>();
	}
	
	/**
	 * Loads all sensors from the database.
	 * 
	 * @return Map with sensors
	 */
	public Map<String, Sensor> loadSensorsFromDb() {
		
		List<Sensor> sensors =  this.sensorRepository.findAll();
		
		this.sensorMap = sensors.stream()
				.collect(Collectors.toMap(Sensor::getUniqueId, sensor -> sensor));
		
		LOG.info("{} sensors loaded from database", this.sensorMap.size());
		
		return this.sensorMap;
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
		        sensorNode = this.insertTypeInState(sensorNode, sensorType);

		        // Convert JsonNode into a Sensor object
		        Sensor sensor = objectMapper.treeToValue(sensorNode, Sensor.class);

		        // Insert sensor with uniqueId as key into the map
		        if (sensor.getUniqueId() != null) {
		        	this.updateSensorMap(sensor);
		            
		            // save sensor
		            this.sensorStateRepository.save(this.sensorMap.get(sensor.getUniqueId()).getState());
		            this.sensorRepository.save(this.sensorMap.get(sensor.getUniqueId()));
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
	
	
	private void updateSensorAttrByJson(Sensor sensor, JsonNode root) {
		// Convert JsonNode into a Sensor object
		JsonNode attrNode = root.get("attr");
		
        try {
			Sensor changedSensor = objectMapper.treeToValue(attrNode, Sensor.class);
			this.modelMapper.map(changedSensor, sensor);
			
			LOG.debug("Sensor {} updated", sensor.getUniqueId());
		} catch (JsonProcessingException | IllegalArgumentException e) {
			LOG.error("Can't create Sensor from JSON: {}", root.toString());
			e.printStackTrace();
		}
	}
	

	public void updateSensorStateByJson(Sensor sensor, JsonNode root) {
		
		root = this.insertTypeInState(root, sensor.getType());
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
	
	
	private JsonNode insertTypeInState(JsonNode root, String sensorType) {
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
	
	
	public void updateSensorMap(Sensor sensor) {
		if (this.sensorMap.containsKey(sensor.getUniqueId())) {
			// key already exists
			// update Sensor
			this.modelMapper.map(sensor, this.sensorMap.get(sensor.getUniqueId()));
			LOG.debug("{} updated",	this.sensorMap.get(sensor.getUniqueId()).getTypeAndIdInfo());
		} else {
			// new sensor
			this.sensorMap.put(sensor.getUniqueId(), sensor);
			LOG.debug("New {} registered ({})", sensor.getClass().getSimpleName(), sensor.getUniqueId());
		}
	}
	
	
	public void addSensor(Sensor sensor) {
		this.sensorMap.put(sensor.getId(), sensor);
	}

}
