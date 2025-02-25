package de.qwyt.housecontrol.tyche.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	
	@Autowired
	public AutomationProfileManager(
			AutomationProfileProperties properties,
			AutomationProfileRepository automationProfileRepository,
			ActiveAutomationProfileRepository activeAutomationProfileRepository
			) {
		this.properties = properties;
		this.automationProfileRepository = automationProfileRepository;
		this.activeAutomationProfileRepository = activeAutomationProfileRepository;
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
	
	public boolean setActiveProfile(AutomationProfileType profileType) {
		if (this.activeProfile.getProfileType() != profileType) {
			// change profile
			this.activeProfile = this.properties.getProfiles().get(profileType);
			this.activeAutomationProfileRepository.save(new ActiveAutomationProfile(this.activeProfile.getProfileType()));
			LOG.info("AUTOMATION PROFILE {} {}", Symbole.ARROW_RIGHT, this.activeProfile.getProfileType());
			
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
