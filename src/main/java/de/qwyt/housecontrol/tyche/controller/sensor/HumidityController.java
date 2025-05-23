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
@RequestMapping("/api/humidity")
public class HumidityController {

	@Autowired
	private SensorServiceImpl service;
	
	@GetMapping(path = "/all/latest")
	public List<SensorState> getLatestHumidityAll() {
		return service.getLatestHumiditySensorStates();
	}
	
	@GetMapping(path = "/{sensorId}/latest")
	public SensorState getLatestHumidityById(@PathVariable String sensorId) {
		return service.getLatestHumiditySensorState(sensorId);
	}
	
	@GetMapping(path = "/{sensorId}")
	public List<SensorState> getHumidityByIdBetween(
			@PathVariable String sensorId,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
			) {
		return service.getHumiditySensorStatesBetween(sensorId, startDate, endDate);
	}
	
	@GetMapping(path = "/{sensorId}/last24h")
	public List<SensorState> getHumidityByIdLast24h(@PathVariable String sensorId) {
		return service.getHumiditySensorStatesLast24h(sensorId);
	}
}
