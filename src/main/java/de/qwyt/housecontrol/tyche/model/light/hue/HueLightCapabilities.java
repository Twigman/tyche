package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HueLightCapabilities {
	
	@Id
	private String id;
	
	@JsonProperty("alerts")
	private List<String> alerts;
	
	@DBRef
	@JsonProperty("color")
	private HueLightColor color;

}
