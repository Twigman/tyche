package de.qwyt.housecontrol.tyche.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;
import de.qwyt.housecontrol.tyche.service.SensorService;

@RestController
@RequestMapping("/api/sensors")
public class SensorRestController {
	
	@Autowired
	private SensorService sensorService;
	
	@GetMapping(path = "/all")
	public Map<String, Sensor> getSensors() {
		return this.sensorService.getSensorMap();
	}

	@GetMapping(path = "/{id}")
	public Sensor getSensorById(@PathVariable String id) {
		return this.sensorService.getSensor(id);
	}
}
