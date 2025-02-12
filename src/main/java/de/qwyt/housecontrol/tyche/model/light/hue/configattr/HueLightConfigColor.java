package de.qwyt.housecontrol.tyche.model.light.hue.configattr;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HueLightConfigColor {
	@JsonProperty("ct")
    private HueLightConfigCt ct;
	
	@JsonProperty("xy")
    private HueLightConfigXy xy;
}
