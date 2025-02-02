package de.qwyt.housecontrol.tyche.controller.sensor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;
import de.qwyt.housecontrol.tyche.repository.sensor.SensorRepository;

@RestController
@RequestMapping("/temperature")
public class TemperatureRestController {

	@Autowired
	private SensorRepository repository;
	
	@GetMapping(path = "/all")
	public List<Sensor> getTemperature() {
		return repository.findAll();
	}
	
	
	@GetMapping(path = "/current")
	public List<Sensor> getCurrentTemperature() {
		return repository.findAll();
	}
}
