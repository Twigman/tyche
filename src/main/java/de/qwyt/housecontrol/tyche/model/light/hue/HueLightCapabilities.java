package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HueLightCapabilities {
	
	@Id
	private String id;
	
	private String sensorId;
	
	@JsonProperty("alerts")
	private List<String> alerts;
	
	@JsonProperty("color")
	private HueLightColor color;

}
