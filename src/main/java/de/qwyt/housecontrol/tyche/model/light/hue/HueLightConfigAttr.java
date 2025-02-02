package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HueLightConfigAttr {
	
	@JsonProperty("groups")
	private List<String> groups;
	
}
