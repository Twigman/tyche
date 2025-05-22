package de.qwyt.housecontrol.tyche.integration.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;

@Component
public class DeconzApiClient {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Value("${deconz.restapi.sensors.url}")
	private String sensorsUrl;
	
	
	@Value("${deconz.restapi.lights.url}")
	private String lightsUrl;
	
	
	@Value("${deconz.restapi.groups.url}")
	private String groupsUrl;
	
	private RestTemplate restTemplate;
	
	private ObjectMapper objectMapper;
	
	private FilterProvider putLightStateFilter;
	
	@Autowired
	public DeconzApiClient(
			RestTemplate restTemplate,
			ObjectMapper objectMapper,
			FilterProvider putLightStateFilter
			) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
		this.putLightStateFilter = putLightStateFilter;
	}
	
	public String getSensors() {
		return restTemplate.getForObject(sensorsUrl, String.class);
	}
	
	public String getLights() {
		return restTemplate.getForObject(lightsUrl, String.class);
	}
	
	public String getGroups() {
		return restTemplate.getForObject(groupsUrl, String.class);
	}
	
	public boolean updateLightState(String id, HueLightState lightState) {
		String url = String.format("%s/%s/state", lightsUrl, id);
		String json = objectToJson(lightState, putLightStateFilter);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
        
        // TODO response contains success or error entries. Handle them!
        //LOG.debug(response.getBody());
        
        if (response.getStatusCode() == HttpStatusCode.valueOf(200)) {
        	return true;
        } else {
        	return true;
        }
	}
	
	private String objectToJson(Object o, FilterProvider f) {
		try {
			return objectMapper.writer(f).writeValueAsString(o);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
