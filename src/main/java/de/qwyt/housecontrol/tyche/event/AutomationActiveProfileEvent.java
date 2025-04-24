package de.qwyt.housecontrol.tyche.event;

import de.qwyt.housecontrol.tyche.event.types.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfileType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutomationActiveProfileEvent extends TycheEvent {

	private static final long serialVersionUID = 8874965415174095105L;
	
	private AutomationProfileType activeProfile;

	public AutomationActiveProfileEvent(Object source, HousecontrolModule module, AutomationProfileType activeProfile) {
		super(source, module);
		this.activeProfile = activeProfile;
	}
}
