package de.qwyt.housecontrol.tyche.model.sensor.zha;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection = "GeneralSensorConfig")
public class GeneralSensorConfig {
	
	@Id
	private String id;
	
	@JsonProperty("battery")
	private int battery; 
	
	@JsonProperty("on")
	private boolean on;
	
	@JsonProperty("reachable")
	private boolean reachable;

}
