package de.qwyt.housecontrol.tyche.model.profile.automation;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import de.qwyt.housecontrol.tyche.model.group.RoomType;
import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "AutomationProfiles")
public class AutomationProfile {
	@Id
	private AutomationProfileType profileType;
	
	private Boolean activeMotionDetection;
	
	private Boolean activeLightAutomation;
	
	// true: switches back to profile HOME, if the room is inactive
	// e.g. for profile cooking ignoreSensors is true so the light stays on
	// with autoHomeProfile the profile switches back to HOME after a certain period of inactivity
	private Boolean autoHomeProfile;
	
	private AutomationProfileSensors sensors;
	
	private Map<RoomType, AutomationProfilePreset> presets;
	
	public Boolean isActiveLightAutomation() {
		return activeLightAutomation;
	}
	
	public Boolean isActiveMotionDetection() {
		return activeMotionDetection;
	}
	
	public Boolean isAutoHomeProfile() {
		return autoHomeProfile;
	}
}
