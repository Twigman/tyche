package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.qwyt.housecontrol.tyche.model.light.hue.configattr.HueLightConfigBri;
import de.qwyt.housecontrol.tyche.model.light.hue.configattr.HueLightConfigColor;
import de.qwyt.housecontrol.tyche.model.light.hue.configattr.HueLightConfigOn;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HueLightConfigAttr {
	
	@JsonProperty("bri")
	private HueLightConfigBri bri;
	
	@JsonProperty("color")
    private HueLightConfigColor color;
	
	@JsonProperty("groups")
	private List<String> groups;
	
	@JsonProperty("on")
	private HueLightConfigOn onAttr;
}