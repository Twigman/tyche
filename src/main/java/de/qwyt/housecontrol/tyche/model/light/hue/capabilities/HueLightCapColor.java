package de.qwyt.housecontrol.tyche.model.light.hue.capabilities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HueLightCapColor {
	
	@JsonProperty("ct")
	private HueLightCapColorCt ct;
	
	@JsonProperty("effects")
	private List<String> effects;
	
	@JsonProperty("gamut_type")
    private String gamutType;
	
	@JsonProperty("modes")
	private List<String> modes;
	
	@JsonProperty("xy")
	private HueLightCapXy xy;
}
