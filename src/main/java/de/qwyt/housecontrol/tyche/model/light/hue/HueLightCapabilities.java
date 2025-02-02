package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HueLightCapabilities {
	
	@JsonProperty("alerts")
	private List<String> alerts;
	
	@JsonProperty("color")
	private HueLightColor color;
}
