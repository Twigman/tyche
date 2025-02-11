package de.qwyt.housecontrol.tyche.model.profile.automation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AutomationProfileSensors {
	
	private Double targetTemperatureLivingroom;
	
	private Double targetRelativeHumidity;

}
