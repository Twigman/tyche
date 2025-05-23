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
@RequestMapping("/api/pressure")
public class PressureController {

	@Autowired
	private SensorServiceImpl service;
	
	@GetMapping(path = "/all/latest")
	public List<SensorState> getLatestPressureAll() {
		return service.getLatestPressureSensorStates();
	}
	
	@GetMapping(path = "/{sensorId}/latest")
	public SensorState getLatestPressureById(@PathVariable String sensorId) {
		return service.getLatestPressureSensorState(sensorId);
	}
	
	@GetMapping(path = "/{sensorId}")
	public List<SensorState> getPressureByIdBetween(
			@PathVariable String sensorId,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
			) {
		return service.getPressureSensorStatesBetween(sensorId, startDate, endDate);
	}
	
	@GetMapping(path = "/{sensorId}/last24h")
	public List<SensorState> getPressureByIdLast24h(@PathVariable String sensorId) {
		return service.getPressureSensorStatesLast24h(sensorId);
	}
}
