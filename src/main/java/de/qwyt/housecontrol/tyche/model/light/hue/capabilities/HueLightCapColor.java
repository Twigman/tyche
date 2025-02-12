package de.qwyt.housecontrol.tyche.model.light.hue.capabilities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightColorCt;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HueLightCapColor {
	
	@JsonProperty("ct")
	private HueLightColorCt ct;
	
	@JsonProperty("effects")
	private List<String> effects;
	
	@JsonProperty("gamut_type")
    private String gamutType;
	
	@JsonProperty("modes")
	private List<String> modes;
	
	@JsonProperty("xy")
	private HueLightCapXy xy;
}
