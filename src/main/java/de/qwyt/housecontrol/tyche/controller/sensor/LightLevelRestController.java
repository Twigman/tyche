package de.qwyt.housecontrol.tyche.controller.sensor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;
import de.qwyt.housecontrol.tyche.service.SensorServiceImpl;

@RestController
@RequestMapping("/api/lightlevel")
public class LightLevelRestController {

	@Autowired
	private SensorServiceImpl service;
	
	@GetMapping(path = "/all/latest")
	public List<SensorState> getLatestLightLevelAll() {
		return service.getLatestLightLevelSensorStates();
	}
	
	@GetMapping(path = "/{sensorId}/latest")
	public SensorState getLatestLightLevelById(@PathVariable String sensorId) {
		return service.getLatestLightLevelSensorState(sensorId);
	}
	
	@GetMapping(path = "/{sensorId}")
	public List<SensorState> getLightLevelByIdBetween(
			@PathVariable String sensorId,
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
			) {
		return service.getLightLevelSensorStatesBetween(sensorId, date, date);
	}
}
