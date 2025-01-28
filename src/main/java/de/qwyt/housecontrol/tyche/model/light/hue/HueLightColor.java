package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HueLightColor {
	
	@Id
	private String id;
	
	private String sensorId;
	
	@JsonProperty("ct")
	private HueLightColorCt ct;
	
	@JsonProperty("effects")
	private List<String> effects;
	
	@JsonProperty("modes")
	private List<String> modes;

}
