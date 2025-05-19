package de.qwyt.housecontrol.tyche.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutoProfileSwitch;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfileType;

@Configuration
public class ProfileConfig {
	
	@Bean
	@Scope("prototype")
	public AutoProfileSwitch autoSwitchProfilePrototype() {
		AutoProfileSwitch profile = new AutoProfileSwitch();
		profile.setEnabled(false);
		profile.setInTimespanSec(0);
		profile.setVisitThreshold(0);
		profile.setToProfile(AutomationProfileType.HOME);
		
		return profile;
	}
	
	@Bean
	@Scope("prototype")
	public HueLightState hueLightStatePrototype() {
		return new HueLightState();
	}
}
