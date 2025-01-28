package de.qwyt.housecontrol.tyche.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DeconzRestController {

	@Value("${deconz.restapi.sensors.url}")
	private String sensorsUrl;
	
	
	@Value("${deconz.restapi.lights.url}")
	private String lightsUrl;
	
	
	@Value("${deconz.restapi.groups.url}")
	private String groupsUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String getSensors() {
		return restTemplate.getForObject(sensorsUrl, String.class);
	}
	
	public String getLights() {
		return restTemplate.getForObject(lightsUrl, String.class);
	}
	
	public String getGroups() {
		return restTemplate.getForObject(groupsUrl, String.class);
	}
}
