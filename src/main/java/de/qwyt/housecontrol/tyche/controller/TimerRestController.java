package de.qwyt.housecontrol.tyche.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.qwyt.housecontrol.tyche.controller.request.TimerRequest;
import de.qwyt.housecontrol.tyche.controller.response.TimerResponseEntry;
import de.qwyt.housecontrol.tyche.service.TimerService;

@RestController
@RequestMapping("/api/timers")
public class TimerRestController {

	private final TimerService timerService;
	
	@Autowired
	public TimerRestController(TimerService timerService) {
		this.timerService = timerService;
	}
	
	
	@PutMapping(path = "/{timerId}")
	public void putTimer(
			@PathVariable String timerId,
			@RequestBody TimerRequest request
			) {
		
	}
	
	@PutMapping(path = "/preset/{timerPreset}/{timerId}")
	public void putTimer(
			@PathVariable String timerId,
			@PathVariable String timerPreset,
			@RequestBody TimerRequest request
			) {
		
	}
	
	@GetMapping(path = "/all")
	public List<TimerResponseEntry> getTimerList() {
		Set<String> timerIds = timerService.getRunningTimerIds();
		List<TimerResponseEntry> propertyList = new ArrayList<>();
		
		for (String id : timerIds) {
			TimerResponseEntry entry = new TimerResponseEntry();
			entry.setId(id);
			timerService.getRemainingTime(id).ifPresent(duration -> entry.setRemainingTime(duration.getSeconds()));
			propertyList.add(entry);
		}
		
		return propertyList;
	}
}
