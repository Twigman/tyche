package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "LightLevelSensorState")
public class LightLevelSensorState extends SensorState {

	@JsonProperty("dark")
	private boolean dark;
	
	@JsonProperty("daylight")
	private boolean daylight;
	
	@JsonProperty("lightlevel")
	private int lightlevel;
	
	@JsonProperty("lux")
	private int lux;
}
