package de.qwyt.housecontrol.tyche.controller.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;
import de.qwyt.housecontrol.tyche.service.LightServiceImpl;

@RestController
@RequestMapping("/api/lights")
public class LightRestController {

	@Autowired
	private LightServiceImpl lightService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${deconz.restapi.lights.url}")
	private String lightsUrl;
	
	
}
