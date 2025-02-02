package de.qwyt.housecontrol.tyche.model.light.hue;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection = "HueLightColorCts")
public class HueLightColorCt {
	
	@Id
	private String id;
	
	@JsonProperty("max")
	private Integer max;
	
	@JsonProperty("min")
	private Integer min;

}
