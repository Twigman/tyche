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
	
	private Boolean activateMotionDetection;
	
	private Boolean activateLightAutomation;
	
	private AutomationProfileSensors sensors;
	
	private Map<RoomType, LightPresets> presets;
}
