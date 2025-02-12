package de.qwyt.housecontrol.tyche.model.light.hue.capabilities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HueLightCapXy {
	@JsonProperty("blue")
	private List<Double> blue;
	
	@JsonProperty("green")
    private List<Double> green;
    
	@JsonProperty("red")
    private List<Double> red;
}
