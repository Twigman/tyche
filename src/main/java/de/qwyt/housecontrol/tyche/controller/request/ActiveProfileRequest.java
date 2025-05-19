package de.qwyt.housecontrol.tyche.controller.request;

import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfileType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveProfileRequest {
	
	private AutomationProfileType type;

}
