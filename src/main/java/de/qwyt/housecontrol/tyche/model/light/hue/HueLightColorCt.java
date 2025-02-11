package de.qwyt.housecontrol.tyche.model.light.hue;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HueLightColorCt {
	
	@Id
	private String id;
	
	@JsonProperty("max")
	private Integer max;
	
	@JsonProperty("min")
	private Integer min;
}
