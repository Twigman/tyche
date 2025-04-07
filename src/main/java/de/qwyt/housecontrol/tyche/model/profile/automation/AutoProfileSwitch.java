package de.qwyt.housecontrol.tyche.model.profile.automation;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AutoProfileSwitch {
	
	private Boolean enabled;
	
	private Integer visitThreshold;
	
	private Integer inTimespanSec;
	
	private AutomationProfileType toProfile;

	
	public Boolean isEnabled() {
		return this.enabled;
	}

}
