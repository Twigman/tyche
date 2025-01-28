package de.qwyt.housecontrol.tyche.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.qwyt.housecontrol.tyche.model.sensor.zha.TemperatureSensor;
import de.qwyt.housecontrol.tyche.repository.TemperatureRepository;

@RestController
@RequestMapping("/temperature")
public class TemperatureRestController {

	@Autowired
	private TemperatureRepository repository;
	
	@GetMapping(path = "/all")
	public List<TemperatureSensor> getTemperature() {
		return repository.findAll();
	}
	
}
