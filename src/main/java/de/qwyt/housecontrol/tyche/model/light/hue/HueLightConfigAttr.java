package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HueLightConfigAttr {
	
	@Id
	private String id;
	
	@JsonProperty("groups")
	private List<String> groups;
	
}
