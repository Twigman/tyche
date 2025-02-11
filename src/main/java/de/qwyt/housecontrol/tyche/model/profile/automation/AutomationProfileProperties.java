package de.qwyt.housecontrol.tyche.model.profile.automation;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import de.qwyt.housecontrol.tyche.model.group.RoomType;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "automation")
public class AutomationProfileProperties {
	
	private AutomationProfileType activeProfile;

	private Map<AutomationProfileType, AutomationProfile> profiles;
	
	@PostConstruct
	public void applyDefaults() {
		for (AutomationProfile profile : profiles.values()) {
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
			// Profile for "ALL"
			if (profile.getPresets().containsKey(RoomType.ALL)) {
				// save default presets
				LightPresets defaultSettings = profile.getPresets().get(RoomType.ALL); 
				profile.getPresets().remove(RoomType.ALL);
				
				for (RoomType type : RoomType.values()) {
					if (type == RoomType.ALL || profile.getPresets().containsKey(type)) {
						// settings for a specific room
						// or ALL
						// skip
					} else {
						// create a new entry with default settings for this room
						profile.getPresets().put(type, defaultSettings);
					}
				}
			}
		}
	}
}
