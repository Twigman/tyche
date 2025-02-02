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

import de.qwyt.housecontrol.tyche.model.light.hue.HueLight;
import de.qwyt.housecontrol.tyche.repository.light.HueLightRepository;
import de.qwyt.housecontrol.tyche.repository.light.HueLightStateRepository;

@Service
public class LightService {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	// uniqueId is the Key
	private Map<String, HueLight> lightMap;
	
	private final ObjectMapper objectMapper;
	
	private final ModelMapper modelMapper;
	
	private final HueLightRepository hueLightRepository;
	
	private final HueLightStateRepository hueLightStateRepository;
	
	@Autowired
	public LightService(
			ObjectMapper objectMapper,
			ModelMapper modelMapper,
			HueLightRepository hueLightRepository,
			HueLightStateRepository hueLightStateRepository
			) {
		this.objectMapper = objectMapper;
		this.modelMapper = modelMapper;
		this.hueLightRepository = hueLightRepository;
		this.hueLightStateRepository = hueLightStateRepository;
		this.lightMap = new HashMap<>();
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
		
		LOG.debug(rootMessage.toPrettyString());
		
		if (rootMessage.has("state")) {
			// state event
			
		} else if (rootMessage.has("attr")) {
			// attr event
			this.updateLightAttrByJson(light, rootMessage);
		} else {
			
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
}
