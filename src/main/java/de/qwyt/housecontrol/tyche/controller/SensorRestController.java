package de.qwyt.housecontrol.tyche.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;
import de.qwyt.housecontrol.tyche.service.SensorServiceImpl;

@RestController
@RequestMapping("/api/sensors")
public class SensorRestController {
	
	@Autowired
	private SensorServiceImpl sensorService;
	
	@GetMapping(path = "/all")
	public Map<String, Sensor> getSensors() {
		return this.sensorService.getSensorMap();
	}

	@GetMapping(path = "/{id}")
	public Sensor getSensorById(@PathVariable String id) {
		return this.sensorService.getSensor(id);
	}
	
	@GetMapping(path = "/temperature")
	public List<Sensor> getTemperatureSensors() {
		return this.sensorService.getTemperatureSensors();
	}
	
	@GetMapping(path = "/presence")
	public List<Sensor> getPresenceSensors() {
		return this.sensorService.getPresenceSensors();
	}
	
	@GetMapping(path = "/humidity")
	public List<Sensor> getHumiditySensors() {
		return this.sensorService.getHumiditySensors();
	}
	
	@GetMapping(path = "/lightlevel")
	public List<Sensor> getLightLevelSensors() {
		return this.sensorService.getLightLevelSensors();
	}
	
	@GetMapping(path = "/pressure")
	public List<Sensor> getPressureSensors() {
		return this.sensorService.getPressureSensors();
	}
}
