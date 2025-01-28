package de.qwyt.housecontrol.tyche.model.sensor.zha;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection = "GeneralSensorConfig")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralSensorConfig {
	
	@Id
	private String id;
	
	@JsonProperty("battery")
	private Integer battery; 
	
	@JsonProperty("on")
	private boolean on;
	
	@JsonProperty("reachable")
	private boolean reachable;

}
