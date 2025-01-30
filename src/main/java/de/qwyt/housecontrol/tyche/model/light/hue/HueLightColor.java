package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HueLightColor {
	
	@Id
	private String id;
	
	@DBRef
	@JsonProperty("ct")
	private HueLightColorCt ct;
	
	@JsonProperty("effects")
	private List<String> effects;
	
	@JsonProperty("modes")
	private List<String> modes;

}
