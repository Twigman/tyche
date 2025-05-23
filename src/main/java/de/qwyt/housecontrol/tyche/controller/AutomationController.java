package de.qwyt.housecontrol.tyche.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.qwyt.housecontrol.tyche.controller.request.ActiveProfileRequest;
import de.qwyt.housecontrol.tyche.event.types.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfileType;
import de.qwyt.housecontrol.tyche.service.AutomationProfileManager;
import de.qwyt.housecontrol.tyche.service.AutomationServiceImpl;

@RestController
@RequestMapping("/api/automation")
public class AutomationController {
	
	private final AutomationProfileManager automationProfileManager;
	
	private final AutomationServiceImpl automationService;
	
	@Autowired
	public AutomationController(
			AutomationProfileManager automationProfileManager, 
			AutomationServiceImpl automationService
			) {
		this.automationProfileManager = automationProfileManager;
		this.automationService = automationService;
		
	}
	
	@GetMapping(path = "/active-profile")
	public AutomationProfileType getActiveProfile() {
		return automationProfileManager.getActiveProfileType();
	}

	@GetMapping(path = "/profiles")
	public AutomationProfileType[] getPhoneInfo() {
		return AutomationProfileType.values();
	}
	
	@PostMapping(path = "/active-profile")
	public ResponseEntity<String> postActiveProfile(@RequestBody ActiveProfileRequest request) {
		boolean success = automationService.setAndExecuteActiveProfile(HousecontrolModule.MANUAL, request.getType());
		
		if (success) {
			return ResponseEntity.ok("Active profile set successfully");
		} else {
			return ResponseEntity.badRequest().body("Profile is already active");
		}
	}
}
