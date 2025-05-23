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

import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;
import de.qwyt.housecontrol.tyche.repository.sensor.SensorRepository;
import de.qwyt.housecontrol.tyche.repository.sensor.SensorStateRepository;
import de.qwyt.housecontrol.tyche.service.SensorServiceImpl;

@RestController
@RequestMapping("/api/temperatures")
public class TemperatureController {

	@Autowired
	private SensorServiceImpl service;
	
	@GetMapping(path = "/all/latest")
	public List<SensorState> getLatestTemperatureAll() {
		return service.getLatestTemperatureSensorStates();
	}
	
	@GetMapping(path = "/{sensorId}/latest")
	public SensorState getLatestTemperatureById(@PathVariable String sensorId) {
		return service.getLatestTemperatureSensorState(sensorId);
	}
	
	// call /api/temperatures/{id}?startDate=2025-03-05&endDate=2025-03-05
	@GetMapping(path = "/{sensorId}")
	public List<SensorState> getTemperatureByIdBetween(
			@PathVariable String sensorId,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
			) {
		return service.getTemperatureSensorStatesBetween(sensorId, startDate, endDate);
	}
	
	@GetMapping(path = "/{sensorId}/last24h")
	public List<SensorState> getTemperatureByIdLast24h(@PathVariable String sensorId) {
		return service.getTemperatureSensorStatesLast24h(sensorId);
	}
}
