package de.qwyt.housecontrol.tyche.model.profile.automation;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LightPresets {

	private HueLightState lights;
	
}
