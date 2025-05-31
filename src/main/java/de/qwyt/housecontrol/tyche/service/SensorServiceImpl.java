package de.qwyt.housecontrol.tyche.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import de.qwyt.housecontrol.tyche.model.sensor.zha.HumiditySensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.LightLevelSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PressureSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.TemperatureSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;
import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorStatesCollectionName;
import de.qwyt.housecontrol.tyche.repository.sensor.SensorConfigRepository;
import de.qwyt.housecontrol.tyche.repository.sensor.SensorRepository;
import de.qwyt.housecontrol.tyche.repository.sensor.SensorStateRepository;
import de.qwyt.housecontrol.tyche.util.TimeHelper;

@Service
public class SensorServiceImpl {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	// uniqueId is the Key
	private Map<String, Sensor> sensorMap;
	
	private final ObjectMapper objectMapper;
	
	private final ModelMapper modelMapper;
	
	private final SensorRepository sensorRepository;
	
	private final SensorStateRepository sensorStateRepository;
	
	private final SensorConfigRepository sensorConfigRepository;
	
	private final EventServiceImpl eventService;
	
	@Autowired
	public SensorServiceImpl(
			ObjectMapper objectMapper,
			@Qualifier("notNullModelMapper") ModelMapper modelMapper,
			SensorRepository sensorRepository,
			SensorStateRepository sensorStateRepository,
			SensorConfigRepository sensorConfigRepository,
			EventServiceImpl eventService
			) {
		this.modelMapper = modelMapper;
		this.objectMapper = objectMapper;
		this.sensorRepository = sensorRepository;
		this.sensorStateRepository = sensorStateRepository;
		this.sensorConfigRepository = sensorConfigRepository;
		this.eventService = eventService;
		this.sensorMap = new HashMap<>();
	}
	
	/**
	 * Loads all sensors from the database and adds them to the registered sensors map.
	 * <p>
	 * This method retrieves all sensor records from the database and adds them to a map where
	 * the key is the uniqueId of each sensor, allowing for efficient lookups of sensors by their
	 * unique identifier.
	 * </p>
	 *
	 * @return A map containing sensors where the key is the uniqueId of each sensor.
	 * @see SensorRepository
	 */
	public Map<String, Sensor> loadSensorsFromDb() {
		
		List<Sensor> sensors =  this.sensorRepository.findAll();
		
		this.sensorMap = sensors.stream()
				.collect(Collectors.toMap(Sensor::getUniqueId, sensor -> sensor));
		
		LOG.info("{} sensors loaded from database", this.sensorMap.size());
		
		return this.sensorMap;
	}
	

	/**
	 * Registers all sensors from a JSON response, including all available attributes such as config and state.
	 * <p>
	 * This method processes the provided JSON string, extracts sensor data, and registers them into the system.
	 * For each sensor, the method checks if it has a uniqueId and updates the sensor map accordingly.
	 * </p>
	 * <p>
	 * Note: Very similar to registerLights
	 * </p>
	 *
	 * @param jsonResponse The JSON string containing the sensor data from deconz.
	 * @return {@code true} if all sensors were successfully registered without errors; {@code false} otherwise.
	 */
	public boolean registerSensors(String jsonResponse) {
		// parse Sensors
		try {
			// parse JSON-String
		    JsonNode rootNode = objectMapper.readTree(jsonResponse);
		    
		    // iterate over all root nodes
		    for (Map.Entry<String, JsonNode> entry : rootNode.properties()) {
		        JsonNode sensorNode = entry.getValue();

		        // Extract the sensor type from the root node
		        String sensorType = sensorNode.get("type").asText();
		        sensorNode = this.insertTypeInNode(sensorNode, sensorType, "state");

		        // Convert JsonNode into a Sensor object
		        Sensor sensor = objectMapper.treeToValue(sensorNode, Sensor.class);

		        // Insert sensor with uniqueId as key into the map
	        	if (this.updateSensorMap(sensor)) {
	        		// save sensor
	        		// assign unique sensor id to sensorState and sensorConfig
	        		this.sensorMap.get(sensor.getUniqueId()).getState().setSensorId(sensor.getUniqueId());
	        		this.sensorMap.get(sensor.getUniqueId()).getConfig().setSensorId(sensor.getUniqueId());
	        		
	        		this.sensorConfigRepository.save(this.sensorMap.get(sensor.getUniqueId()).getConfig());
		            this.sensorStateRepository.saveSensorState(this.sensorMap.get(sensor.getUniqueId()).getState());
		            this.sensorRepository.save(this.sensorMap.get(sensor.getUniqueId()));
		        } else {
		            // TODO
		        }
		    }
			LOG.info(this.sensorMap.size() + " sensors registered");
			
			return true;
			
		} catch (JsonProcessingException e) {
			LOG.error("Error during Sensor registration");
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Updates a sensor by processing a JSON message and applying the changes to the sensor's state or attributes.
	 * <p>
	 * The method determines if the event contains a state or attribute update and processes it accordingly.
	 * </p>
	 *
	 * @param uniqueId The unique identifier of the sensor to be updated.
	 * @param rootMessage The JSON message containing the sensor update data.
	 */
	public void updateSensorByJson(String uniqueId, JsonNode rootMessage) {
		Sensor sensor = this.sensorMap.get(uniqueId);
		
		if (rootMessage.has("state")) {
			// state event
			this.updateSensorStateByJson(sensor, rootMessage);
		} else if (rootMessage.has("attr")) {
			// attr event
			this.updateSensorAttrByJson(sensor, rootMessage);
		} else if (rootMessage.has("config")) {
			// config event
			LOG.warn("Not implemented config Event: {}", rootMessage.toString());
		} else {
			LOG.warn("Event doesn't contains a state- or attr-Attribute: {}", rootMessage.toString());
		}
	}
	
	
	/**
	 * Updates a sensor's attributes based on the provided JSON data.
	 * <p>
	 * This method processes the provided JSON node containing sensor attributes and maps the values to
	 * the existing sensor objecgetClass().getSimpleName()t. It then updates the sensor in the system.
	 * </p>
	 *
	 * @param sensor The sensor to be updated.
	 * @param root The JSON node containing the attribute data for the sensor.
	 */
	private boolean updateSensorAttrByJson(Sensor sensor, JsonNode root) {
		// Convert JsonNode into a Sensor object
		JsonNode attrNode = root.get("attr");
		
        try {
        	// type is necessary for mapping
        	root = this.insertTypeInNode(attrNode, sensor.getType(), "");
        	
			Sensor changedSensor = objectMapper.treeToValue(attrNode, Sensor.class);
			
			// only update if relevant fields have changed
			if (sensor.hasChangedValuesRelevantForDatabase(changedSensor)) {
				// update
				this.modelMapper.map(changedSensor, sensor);
				this.sensorRepository.save(sensor);
				LOG.debug("{} updated", sensor.getNameAndIdInfo());
				return true;
			} else {
				// no changes
				LOG.debug("No relevant changes for {}", sensor.getNameAndIdInfo());
				return false;
			}
		} catch (JsonProcessingException | IllegalArgumentException e) {
			LOG.error("Can't create Sensor from JSON: {}", root.toString());
			e.printStackTrace();
		}
        return false;
	}
	

	/**
	 * Updates a sensor's state based on the provided JSON data.
	 * <p>
	 * This method processes the provided JSON node containing the state data for the sensor, updates
	 * the sensor's state, and applies the changes to the registered sensor.
	 * </p>
	 *
	 * @param sensor The sensor whose state is to be updated.
	 * @param root The JSON node containing the state data for the sensor.
	 */
	private void updateSensorStateByJson(Sensor sensor, JsonNode root) {
		
		root = this.insertTypeInNode(root, sensor.getType(), "state");
		// Map state-entry to concrete SensorState and change values of the registered sensor
		JsonNode stateNode = root.get("state");

		try {
		    // Deserialize stateNode to a concrete SensorState instance
		    SensorState changedSensorState = this.objectMapper.treeToValue(stateNode, sensor.getState().getClass());
		    
		    // Update sensor state only for non-null attributes
		    this.modelMapper.map(changedSensorState, sensor.getState());
		    // reset sensorState ID to add a new entry to the database
		 	sensor.getState().setId(null);
		 	this.sensorStateRepository.saveSensorState(sensor.getState());
		    LOG.debug("State changed for {}", sensor.getNameAndIdInfo());
		    
		    // Event Service
		    eventService.fireSensorEvent(sensor);
		} catch (JsonProcessingException | IllegalArgumentException e) {
		    LOG.error("Can't create SensorState from JSON: {}", stateNode.toString());
		    e.printStackTrace();
		}
	}
	

	/**
	 * Inserts a {@code type} field into the {@code state} node of the given JSON object.
	 * <p>
	 * If the {@code state} field exists and is an object, the method adds a new field called
	 * {@code type} with the provided {@code sensorType} as its value. If the {@code state} field
	 * is missing or not an object, a warning is logged.
	 * </p>
	 *
	 * @param root the root {@link JsonNode} containing the {@code state} field
	 * @param sensorType the type of the sensor to be inserted into the {@code state} node
	 * @return the modified {@link JsonNode} with the {@code type} field added to the {@code state} node
	 * @throws NullPointerException if {@code root} is {@code null}
	 */
	private JsonNode insertTypeInNode(JsonNode root, String sensorType, String targetNode) {
		if (targetNode == "") {
			if (root != null && root.isObject()) {
				((ObjectNode) root).put("type", sensorType);
			} else {
				LOG.warn("root is null");
			}
		} else {
			JsonNode node = root.get(targetNode);
			
			// Add type in node if it is an ObjectNode
			if (node != null && node.isObject()) {
				((ObjectNode) node).put("type", sensorType);
			    ((ObjectNode) root).set(targetNode, node);
			} else {
				LOG.warn("Sensor has no '" + targetNode + "' attribute.");
			}
		}
		
		return root;
	}
	
	/**
	 * Updates the sensor map by adding a new sensor or updating an existing one.
	 * <p>
	 * This method checks if the sensor has a valid uniqueId. If the sensor is already present in the map,
	 * it updates the existing sensor's data. If the sensor is not present in the map, it is added as a new sensor.
	 * If the sensor's uniqueId is null or the sensor object itself is null, an error is logged.
	 * </p>
	 *
	 * @param sensor The sensor to be added or updated in the map.
	 * @return {@code true} if the sensor was successfully added or updated; {@code false} if the sensor is null
	 *         or has a null uniqueId.
	 */
	public boolean updateSensorMap(Sensor sensor) {
		if (sensor != null) {
			if (sensor.getUniqueId() != null) {
				if (this.sensorMap.containsKey(sensor.getUniqueId())) {
					// key already exists
					// update Sensor
					this.modelMapper.map(sensor, this.sensorMap.get(sensor.getUniqueId()));
					LOG.debug("{} updated",	this.sensorMap.get(sensor.getUniqueId()).getNameAndIdInfo());
				} else {
					// new sensor
					this.sensorMap.put(sensor.getUniqueId(), sensor);
					LOG.debug("New {} registered ({})", sensor.getClass().getSimpleName(), sensor.getUniqueId());
				}
				return true;
			} else {
				LOG.error("No unique ID found for sensor " + sensor.getName() + " (" + sensor.getManufacturer() + ")");
			}
		} else {
			LOG.error("Sensor is null");
		}
		return false;
	}
	
	
	public Map<String, Sensor> getSensorMap() {
		return this.sensorMap;
	}
	
	
	public Sensor getSensor(String uniqueId) {
		return this.sensorMap.get(uniqueId);
	}
	
	// SensorState - Latest
	public SensorState getLatestTemperatureSensorState(String sensorId) {
		return sensorStateRepository.findLatestSensorState(sensorId, SensorStatesCollectionName.TEMPERATURE_SENSOR_STATES);
	}
	
	public SensorState getLatestPressureSensorState(String sensorId) {
		return sensorStateRepository.findLatestSensorState(sensorId, SensorStatesCollectionName.PRESSURE_SENSOR_STATES);
	}
	
	public SensorState getLatestPresenceSensorState(String sensorId) {
		return sensorStateRepository.findLatestSensorState(sensorId, SensorStatesCollectionName.PRESENCE_SENSOR_STATES);
	}
	
	public SensorState getLatestLightLevelSensorState(String sensorId) {
		return sensorStateRepository.findLatestSensorState(sensorId, SensorStatesCollectionName.LIGHT_LEVEL_SENSOR_STATES);
	}
	
	public SensorState getLatestHumiditySensorState(String sensorId) {
		return sensorStateRepository.findLatestSensorState(sensorId, SensorStatesCollectionName.HUMIDITY_SENSOR_STATES);
	}
	
	public SensorState getLatestDimmerSwitchState(String sensorId) {
		return sensorStateRepository.findLatestSensorState(sensorId, SensorStatesCollectionName.DIMMER_SWITCH_SENSOR_STATES);
	}

	// All latest sensors states
	public List<SensorState> getLatestPressureSensorStates() {
		return getLatestSensorStatesByClass(PressureSensor.class, SensorStatesCollectionName.PRESSURE_SENSOR_STATES);
	}
	
	public List<SensorState> getLatestLightLevelSensorStates() {
		return getLatestSensorStatesByClass(LightLevelSensor.class, SensorStatesCollectionName.LIGHT_LEVEL_SENSOR_STATES);
	}
	
	public List<SensorState> getLatestPresenceSensorStates() {
		return getLatestSensorStatesByClass(PresenceSensor.class, SensorStatesCollectionName.PRESENCE_SENSOR_STATES);
	}
	
	public List<SensorState> getLatestHumiditySensorStates() {
		return getLatestSensorStatesByClass(HumiditySensor.class, SensorStatesCollectionName.HUMIDITY_SENSOR_STATES);
	}
	
	public List<SensorState> getLatestTemperatureSensorStates() {
		return getLatestSensorStatesByClass(TemperatureSensor.class, SensorStatesCollectionName.TEMPERATURE_SENSOR_STATES);
	}
	
	public List<SensorState> getLatestSensorStatesByClass(Class c, SensorStatesCollectionName collection) {
		List<SensorState> states = new ArrayList<SensorState>();
		
		this.sensorMap.forEach((key, sensor) -> {
			if (sensor.getClass().equals(c)) {
				SensorState state = sensorStateRepository.findLatestSensorState(key, collection);
				
				if (state != null) {
					states.add(state);
				}
			}
		});
		
		return states;
	}

	// SensorStates - Between
	public List<SensorState> getLightLevelSensorStatesBetween(String sensorId, LocalDate startDate, LocalDate endDate) {
		return getSensorStatesBetween(sensorId, startDate, endDate, SensorStatesCollectionName.LIGHT_LEVEL_SENSOR_STATES);
	}
	
	public List<SensorState> getPresenceSensorStatesBetween(String sensorId, LocalDate startDate, LocalDate endDate) {
		return getSensorStatesBetween(sensorId, startDate, endDate, SensorStatesCollectionName.PRESENCE_SENSOR_STATES);
	}
	
	public List<SensorState> getPressureSensorStatesBetween(String sensorId, LocalDate startDate, LocalDate endDate) {
		return getSensorStatesBetween(sensorId, startDate, endDate, SensorStatesCollectionName.PRESSURE_SENSOR_STATES);
	}
	
	public List<SensorState> getHumiditySensorStatesBetween(String sensorId, LocalDate startDate, LocalDate endDate) {
		return getSensorStatesBetween(sensorId, startDate, endDate, SensorStatesCollectionName.HUMIDITY_SENSOR_STATES);
	}
	
	public List<SensorState> getTemperatureSensorStatesBetween(String sensorId, LocalDate startDate, LocalDate endDate) {
		return getSensorStatesBetween(sensorId, startDate, endDate, SensorStatesCollectionName.TEMPERATURE_SENSOR_STATES);
	}
	
	public List<SensorState> getSensorStatesBetween(String sensorId, LocalDate startDate, LocalDate endDate, SensorStatesCollectionName collection) {
		Instant startOfDay = TimeHelper.getStartOfDayUTCPlus1(startDate);
        Instant endOfDay = TimeHelper.getEndOfDayUTCPlus1(endDate);
        
		return sensorStateRepository.findSensorStateBetween(sensorId, startOfDay, endOfDay, collection);
	}

	// last 24h
	public List<SensorState> getTemperatureSensorStatesLast24h(String sensorId) {
		return getSensorStatesLast24h(sensorId, SensorStatesCollectionName.TEMPERATURE_SENSOR_STATES);
	}
	
	public List<SensorState> getHumiditySensorStatesLast24h(String sensorId) {
		return getSensorStatesLast24h(sensorId, SensorStatesCollectionName.HUMIDITY_SENSOR_STATES);
	}
	
	public List<SensorState> getPressureSensorStatesLast24h(String sensorId) {
		return getSensorStatesLast24h(sensorId, SensorStatesCollectionName.PRESSURE_SENSOR_STATES);
	}
	
	public List<SensorState> getLightLevelSensorStatesLast24h(String sensorId) {
		return getSensorStatesLast24h(sensorId, SensorStatesCollectionName.LIGHT_LEVEL_SENSOR_STATES);
	}
	
	public List<SensorState> getSensorStatesLast24h(String sensorId, SensorStatesCollectionName collection) {
		Instant now = Instant.now();
        Instant start = now.minus(Duration.ofHours(24));
        
		return sensorStateRepository.findSensorStateBetween(sensorId, start, now, collection);
	}
	
	public List<Sensor> getSensorsByClass(Class c) {
		List<Sensor> sensorList = new ArrayList<Sensor>();
		
		this.sensorMap.forEach((key, sensor) -> {
			if (sensor.getClass().equals(c)) {
				sensorList.add(sensor);
			}
		});
		
		return sensorList;
	}

	// Sensors by Type
	public List<Sensor> getTemperatureSensors() {
		return getSensorsByClass(TemperatureSensor.class);
	}

	public List<Sensor> getPresenceSensors() {
		return getSensorsByClass(PresenceSensor.class);
	}

	public List<Sensor> getHumiditySensors() {
		return getSensorsByClass(HumiditySensor.class);
	}

	public List<Sensor> getLightLevelSensors() {
		return getSensorsByClass(LightLevelSensor.class);
	}

	public List<Sensor> getPressureSensors() {
		return getSensorsByClass(PressureSensor.class);
	}

}
