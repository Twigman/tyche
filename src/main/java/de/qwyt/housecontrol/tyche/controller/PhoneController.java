package de.qwyt.housecontrol.tyche.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.qwyt.housecontrol.tyche.controller.validator.DPoPValidator;
import de.qwyt.housecontrol.tyche.model.device.PhoneInfo;
import de.qwyt.housecontrol.tyche.service.AutomationServiceImpl;
import de.qwyt.housecontrol.tyche.service.PhoneServiceImpl;

@RestController
@RequestMapping("/api/phone")
public class PhoneController {

	@Autowired
	private PhoneServiceImpl phoneService;
	
	@Autowired
	private AutomationServiceImpl automationService;
	
	@Value("${tyche.url}")
	private String url;
	
	
	private final DPoPValidator dpopValidator;
	
	public PhoneController(DPoPValidator dpopValidator) {
		this.dpopValidator = dpopValidator;
	}
	
	@GetMapping(path = "/info")
	public PhoneInfo getPhoneInfo(@RequestHeader("DPoP") String dpopHeader) {
		dpopValidator.validateDPoPToken(dpopHeader, url + "/api/phone/info", "GET");
		return this.phoneService.getPhoneInfo();
	}
	
	@GetMapping(path = "/updateInfo")
	public PhoneInfo getPhoneInfoUpdate() {
		this.automationService.checkPhoneConnectionBurst();
		
		return this.phoneService.getPhoneInfo();
	}
}
