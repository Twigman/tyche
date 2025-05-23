package de.qwyt.housecontrol.tyche.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.qwyt.housecontrol.tyche.controller.request.TimerRequest;
import de.qwyt.housecontrol.tyche.controller.response.TimerCreatedResponse;
import de.qwyt.housecontrol.tyche.controller.response.TimerEntryResponse;
import de.qwyt.housecontrol.tyche.controller.validator.DPoPValidator;
import de.qwyt.housecontrol.tyche.service.TimerServiceImpl;

@RestController
@RequestMapping("/api/timers")
public class TimerController {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final TimerServiceImpl timerService;
	
	private final DPoPValidator dpopValidator;
	
	@Value("${tyche.url}")
	private String url;
	
	@Autowired
	public TimerController(
			TimerServiceImpl timerService,
			DPoPValidator dpopValidator
			) {
		this.timerService = timerService;
		this.dpopValidator = dpopValidator;
	}
	
	
	@PostMapping
	public TimerEntryResponse postTimer(
			@RequestHeader("DPoP") String dpopHeader,
			@RequestBody TimerRequest request
			) {
		dpopValidator.validateDPoPToken(dpopHeader, url + "/api/timers", "POST");
		// timer type
		// time
		// event
		// name

		LOG.debug("label: " + request.getLabel());
		LOG.debug("type: " + request.getType());
		LOG.debug("time: " + request.getDelayString());
		LOG.debug("action: " + request.getAction().name());
		LOG.debug("msg: " + request.getMessage());
		
		// test
		TimerEntryResponse r = new TimerEntryResponse();
		r.setId("123-abc");
		r.setRemainingTime(0);
		
		return r;
	}
	
	/*
	@PutMapping(path = "/preset/{timerPreset}/{timerId}")
	public void putTimerPreset(
			@PathVariable String timerId,
			@PathVariable String timerPreset,
			@RequestBody TimerRequest request
			) {
		
	}*/
	
	@GetMapping(path = "/all")
	public List<TimerEntryResponse> getTimerList() {
		Set<String> timerIds = timerService.getRunningTimerIds();
		List<TimerEntryResponse> propertyList = new ArrayList<>();
		
		for (String id : timerIds) {
			TimerEntryResponse entry = new TimerEntryResponse();
			entry.setId(id);
			timerService.getRemainingTime(id).ifPresent(duration -> entry.setRemainingTime(duration.getSeconds()));
			propertyList.add(entry);
		}
		
		return propertyList;
	}
}
