package de.qwyt.housecontrol.tyche.model.light.hue.capabilities;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HueLightCapColorCt {
	
	@Id
	private String id;
	
	@JsonProperty("computes_xy")
    private Boolean computesXy;
	
	@JsonProperty("max")
	private Integer max;
	
	@JsonProperty("min")
	private Integer min;
}
