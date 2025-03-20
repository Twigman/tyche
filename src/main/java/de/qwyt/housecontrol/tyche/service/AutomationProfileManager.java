package de.qwyt.housecontrol.tyche.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.event.LogLevel;
import de.qwyt.housecontrol.tyche.model.profile.automation.ActiveAutomationProfile;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfile;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfileProperties;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfileType;
import de.qwyt.housecontrol.tyche.repository.profile.ActiveAutomationProfileRepository;
import de.qwyt.housecontrol.tyche.repository.profile.AutomationProfileRepository;
import de.qwyt.housecontrol.tyche.util.Symbole;
import jakarta.annotation.PostConstruct;

@Component
public class AutomationProfileManager {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final AutomationProfileProperties properties;
	
	private final AutomationProfileRepository automationProfileRepository;
	
	private final ActiveAutomationProfileRepository activeAutomationProfileRepository;
	
	private AutomationProfile activeProfile;
	
	private final EventServiceImpl eventService;

	
	@Autowired
	public AutomationProfileManager(
			AutomationProfileProperties properties,
			AutomationProfileRepository automationProfileRepository,
			ActiveAutomationProfileRepository activeAutomationProfileRepository,
			EventServiceImpl eventService
			) {
		this.properties = properties;
		this.automationProfileRepository = automationProfileRepository;
		this.activeAutomationProfileRepository = activeAutomationProfileRepository;
		this.eventService = eventService;
	}
	
	@PostConstruct
	public void updateProfilesInDatabase() {
		// Set AutomationProfileType
		properties.getProfiles().forEach((type, profile) -> {
			profile.setProfileType(type);
		});
		
		automationProfileRepository.saveAll(properties.getProfiles().values());
		LOG.info("{} automation profiles updated in the database", automationProfileRepository.count());
		this.activeProfile = properties.getProfiles().get(properties.getActiveProfile());
		LOG.info("AUTOMATION PROFILE {} {}", Symbole.ARROW_RIGHT, this.activeProfile.getProfileType());
	}

	public AutomationProfile getProfile(AutomationProfileType profileType) {
		return properties.getProfiles().get(profileType);
	}
	
	public boolean setActiveProfile(HousecontrolModule module, AutomationProfileType profileType) {
		if (this.activeProfile.getProfileType() != profileType) {
			// change profile
			this.activeProfile = this.properties.getProfiles().get(profileType);
			this.activeAutomationProfileRepository.save(new ActiveAutomationProfile(this.activeProfile.getProfileType()));
			LOG.info("AUTOMATION PROFILE {} {}", Symbole.ARROW_RIGHT, this.activeProfile.getProfileType());
			eventService.fireAutomationActiveProfile(module, activeProfile.getProfileType());
			
			if (this.activeProfile.isAutoHomeProfile()) {
				LOG.info("Auto HOME is enabled");
			}
			
			return true;
		}
		
		return false;
	}
	
	public AutomationProfile getActiveProfile() {
		return this.activeProfile;
	}
	
	public AutomationProfileType getActiveProfileType() {
		return this.activeProfile.getProfileType();
	}
}
