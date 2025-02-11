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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.qwyt.housecontrol.tyche.model.group.Room;
import de.qwyt.housecontrol.tyche.model.light.hue.HueLight;
import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;
import de.qwyt.housecontrol.tyche.model.profile.automation.LightPresets;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfileType;
import de.qwyt.housecontrol.tyche.repository.light.HueLightRepository;
import de.qwyt.housecontrol.tyche.repository.light.HueLightStateRepository;
import de.qwyt.housecontrol.tyche.util.Symbole;

@Service
public class LightServiceImpl {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	// uniqueId is the Key
	private Map<String, HueLight> lightMap;
	
	private final ObjectMapper objectMapper;
	
	private final ModelMapper modelMapper;
	
	private final HueLightRepository hueLightRepository;
	
	private final HueLightStateRepository hueLightStateRepository;
	
	private final DeconzApiClient deconzApiClient;
	
	private final HueColorProfileManager colorProfileManager;
	
	@Autowired
	public LightServiceImpl(
			ObjectMapper objectMapper,
			@Qualifier("notNullModelMapper") ModelMapper modelMapper,
			HueLightRepository hueLightRepository,
			HueLightStateRepository hueLightStateRepository,
			DeconzApiClient deconzApiClient, 
			HueColorProfileManager colorProfileManager
			) {
		this.objectMapper = objectMapper;
		this.modelMapper = modelMapper;
		this.hueLightRepository = hueLightRepository;
		this.hueLightStateRepository = hueLightStateRepository;
		this.deconzApiClient = deconzApiClient;
		this.lightMap = new HashMap<>();
		this.colorProfileManager = colorProfileManager;
	}
	
	
	public Map<String, HueLight> loadLightsFromDb() {
		
		List<HueLight> lights =  this.hueLightRepository.findAll();
		
		this.lightMap = lights.stream()
				.collect(Collectors.toMap(HueLight::getUniqueId, light -> light));
		
		LOG.info("{} lights loaded from database", this.lightMap.size());
		
		return this.lightMap;
	}
	
	/**
	 * Registers all lights from a JSON response, including all available attributes.
	 * <p>
	 * This method processes the provided JSON string, extracts light data, and registers them into the system.
	 * For each light, the method checks if it has a uniqueId and updates the light map accordingly.
	 * </p>
	 *
	 * @param jsonResponse The JSON string containing the light data from deconz.
	 * @return {@code true} if all lights were successfully registered without errors; {@code false} otherwise.
	 */
	public boolean registerLights(String jsonResponse) {
		// parse Sensors
		try {
			// parse JSON-String
		    JsonNode rootNode = objectMapper.readTree(jsonResponse);
		    
		    // iterate over all root nodes
		    for (Iterator<Map.Entry<String, JsonNode>> it = rootNode.fields(); it.hasNext(); ) {
		        Map.Entry<String, JsonNode> entry = it.next();
		        JsonNode lightNode = entry.getValue();

		        // Convert JsonNode into a hue light object
		        HueLight light = objectMapper.treeToValue(lightNode, HueLight.class);

		        // Insert light with uniqueId as key into the map
	        	if (this.updateLightMap(light)) {
	        		HueLight l = this.lightMap.get(light.getUniqueId());
	        		// set id
	        		l.getState().setLightId(l.getUniqueId());
	        		// save light
	        		this.hueLightStateRepository.save(l.getState());
	        		this.hueLightRepository.save(l);
		        } else {
		            // TODO
		        }
		    }
			LOG.info(this.lightMap.size() + " sensors registered");
			
			return true;
			
		} catch (JsonProcessingException e) {
			LOG.error("Error during Sensor registration");
			e.printStackTrace();
			return false;
		}
	}
	

	private boolean updateLightMap(HueLight light) {
		if (light != null) {
			if (light.getUniqueId() != null) {
				if (this.lightMap.containsKey(light.getUniqueId())) {
					// key already exists
					// update light
					this.modelMapper.map(light, this.lightMap.get(light.getUniqueId()));
					LOG.debug("{} updated",	this.lightMap.get(light.getUniqueId()).getNameAndIdInfo());
				} else {
					// new light
					this.lightMap.put(light.getUniqueId(), light);
					LOG.debug("{} registered", light.getNameAndIdInfo());
				}
				return true;
			} else {
				LOG.error("No unique ID found for light " + light.getName() + " (" + light.getManufacturer() + ")");
			}
		} else {
			LOG.error("List is null");
		}
		return false;
	}


	public void updateLightByJson(String uniqueId, JsonNode rootMessage) {
		HueLight light = this.lightMap.get(uniqueId);
		
		if (rootMessage.has("state")) {
			// state event
			this.updateLightStateByJson(light, rootMessage);
		} else if (rootMessage.has("attr")) {
			// attr event
			this.updateLightAttrByJson(light, rootMessage);
		} else {
			
		}
	}


	private void updateLightStateByJson(HueLight light, JsonNode root) {
		// Map state-entry to concrete SensorState and change values of the registered sensor
		JsonNode stateNode = root.get("state");

		try {
		    // Deserialize stateNode to a concrete SensorState instance
			HueLightState changedLightState = this.objectMapper.treeToValue(stateNode, HueLightState.class);
		    
		    // Update sensor state only for non-null attributes
		    this.modelMapper.map(changedLightState, light.getState());
		    this.hueLightStateRepository.saveNew(light.getState());
		    LOG.debug("State changed for {}", light.getNameAndIdInfo());
		} catch (JsonProcessingException | IllegalArgumentException e) {
		    LOG.error("Can't create SensorState from JSON: {}", stateNode.toString());
		    e.printStackTrace();
		}
	}


	// TODO 1:1 like the method in SensorService
	private boolean updateLightAttrByJson(HueLight light, JsonNode root) {
		// Convert JsonNode into a Sensor object
		JsonNode attrNode = root.get("attr");
		
        try {
			HueLight changedLight = objectMapper.treeToValue(attrNode, HueLight.class);
			
			// only update if relevant fields have changed
			if (light.hasChangedValuesRelevantForDatabase(changedLight)) {
				// update
				this.modelMapper.map(changedLight, light);
				this.hueLightRepository.save(light);
				LOG.debug("{} updated", light.getNameAndIdInfo());
				return true;
			} else {
				// no changes
				LOG.debug("No relevant changes for {}", light.getNameAndIdInfo());
				return false;
			}
        } catch (JsonProcessingException | IllegalArgumentException e) {
			LOG.error("Can't create Light from JSON: {}", root.toString());
			e.printStackTrace();
		}
        return false;
	}
	
	public boolean turnOnLightsIn(Room room, HueColorProfileType colorProfile) {
		int counter = 0;
		
		for (String lightId : room.getLightIdList()) {
			if (this.turnOnLight(lightId, colorProfile)) {
				counter++;
			}
		}
		LOG.debug("[Light] {}\t{} ON", room.getName(), Symbole.ARROW_RIGHT);
		
		return (counter == room.getLightIdList().size()) ? true : false;
	}
	
	public boolean turnOnLight(String uniqueId, HueColorProfileType colorProfile) {
		HueLight light = this.lightMap.get(uniqueId);
		colorProfileManager.applyProfile(light.getState(), colorProfile);
		
		light.getState().setOn(true);
		
		if (deconzApiClient.updateLightState(uniqueId, light.getState())) {
			this.hueLightStateRepository.saveNew(light.getState());
			LOG.debug("[Light] {} {} ON", light.getName(), Symbole.ARROW_RIGHT);
			return true;
		} else {
			LOG.error("Could not turn on {}", light.getNameAndIdInfo());
			return false;
		}
	}
	
	public boolean updateLight(String uniqueId, HueLightState newState, HueColorProfileType colorProfile) {
		HueLight light = this.lightMap.get(uniqueId);
		this.modelMapper.map(newState, light.getState());
		colorProfileManager.applyProfile(light.getState(), colorProfile);
		
		if (deconzApiClient.updateLightState(uniqueId, light.getState())) {
			this.hueLightStateRepository.saveNew(light.getState());
			LOG.debug("[Light] {} updated", light.getNameAndIdInfo());
			return true;
		} else {
			LOG.error("Could not turn on {}", light.getNameAndIdInfo());
			return false;
		}
	}
	
	public boolean updateLightsIn(Room room, HueLightState newState, HueColorProfileType colorProfile) {
		int counter = 0;
		
		for (String lightId : room.getLightIdList()) {
			if (this.updateLight(lightId, newState, colorProfile)) {
				counter++;
			}
		}
		LOG.debug("[Light] {} updated", room.getName());
		
		return (counter == room.getLightIdList().size()) ? true : false;
	}
	
	public boolean turnOffLight(String uniqueId) {
		HueLight light = this.lightMap.get(uniqueId);
		light.getState().setOn(false);
		
		if (deconzApiClient.updateLightState(uniqueId, light.getState())) {
		 	this.hueLightStateRepository.saveNew(light.getState());
		 	LOG.debug("[Light] {} {} OFF", light.getName(), Symbole.ARROW_RIGHT);
			return true;
		} else {
			LOG.error("Could not turn off {}", light.getNameAndIdInfo());
			return false;
		}
	}
	
	public boolean turnOffLightsIn(Room room) {
		int counter = 0;
		
		for (String lightId : room.getLightIdList()) {
			if (this.turnOffLight(lightId)) {
				counter++;
			}
		}
		LOG.debug("[Light] {}\t{} OFF", room.getName(), Symbole.ARROW_RIGHT);
		
		return (counter == room.getLightIdList().size()) ? true : false;
	}
	
	/*
	private String formatLightName(String name) {
		return String.format("%-21s", name);
	}*/
	
	public Map<String, HueLight> getLightMap() {
		return this.lightMap;
	}
}
