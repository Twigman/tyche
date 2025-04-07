package de.qwyt.housecontrol.tyche.model.profile.automation;

import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import de.qwyt.housecontrol.tyche.model.group.RoomType;
import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfileType;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "automation")
public class AutomationProfileProperties {
	
	private AutomationProfileType activeProfile;

	private Map<AutomationProfileType, AutomationProfile> profiles;
	
	@Autowired
	private ObjectProvider<AutoProfileSwitch> autoSwitchProfileProvider;
	
	@Autowired
	private ObjectProvider<HueLightState> hueLightStateProvider;
	
	@PostConstruct
	public void applyDefaults() {
		for (AutomationProfile profile : profiles.values()) {
			// Iterate over every profile
			
			if (profile.getSensors() == null) {
				// create sensors
				profile.setSensors(new AutomationProfileSensors());
			}
			// set defaults
			if (profile.getSensors().getTargetRelativeHumidity() == null) {
				// Humidity
				profile.getSensors().setTargetRelativeHumidity(50d);
			}
			if (profile.getSensors().getTargetTemperatureLivingroom() == null) {
				// Temperature
				profile.getSensors().setTargetTemperatureLivingroom(18d);
			}
			if (profile.isAutoHomeProfile() == null) {
				profile.setAutoHomeProfile(false);
			}
			// defaults for rooms
			for (RoomType type : RoomType.values()) {
				if (profile.getPresets().containsKey(type)) {
					if (profile.getPresets().get(type).getLights() == null) {
						// create hue light state
						profile.getPresets().get(type).setLights(hueLightStateProvider.getObject());;	
					}
					if (profile.getPresets().get(type).getLights().getColorProfile() == null) {
						// Default color profile
						profile.getPresets().get(type).getLights().setColorProfile(HueColorProfileType.DEFAULT_CT_BRI);
					}
					if (profile.getPresets().get(type).getLights().getIgnoreSensors() == null) {
						profile.getPresets().get(type).getLights().setIgnoreSensors(false);
					}
					// set autoSwitchProfile
					if (profile.getPresets().get(type).getAutoProfileSwitch() == null) {
						// disable
						profile.getPresets().get(type).setAutoProfileSwitch(autoSwitchProfileProvider.getObject());
					}
				}
			}
			
			// Profile for "ALL"
			if (profile.getPresets().containsKey(RoomType.ALL)) {
				// save default presets
				AutomationProfilePreset defaultSettings = profile.getPresets().get(RoomType.ALL); 
				profile.getPresets().remove(RoomType.ALL);
				
				for (RoomType type : RoomType.values()) {
					if (type == RoomType.ALL || profile.getPresets().containsKey(type)) {
						// settings for a specific room
						// or ALL
						// skip
					} else {
						// create a new entry with default settings for this room
						profile.getPresets().put(type, defaultSettings);
						profile.getPresets().get(type).setAutoProfileSwitch(autoSwitchProfileProvider.getObject());
					}
				}
			}
			
		}
	}
}
