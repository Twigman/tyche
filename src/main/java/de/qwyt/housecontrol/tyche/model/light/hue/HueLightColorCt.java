package de.qwyt.housecontrol.tyche.model.light.hue;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HueLightColorCt {
	
	@Id
	private String id;
	
	private String sensorId;
	
	@JsonProperty("max")
	private int max;
	
	@JsonProperty("min")
	private int min;

}
