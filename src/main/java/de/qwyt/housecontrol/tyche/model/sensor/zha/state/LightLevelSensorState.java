package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Document(collection = "LightLevelSensorStates")
public class LightLevelSensorState extends SensorState {

	@JsonProperty("dark")
	private boolean dark;
	
	@JsonProperty("daylight")
	private boolean daylight;
	
	@JsonProperty("lightlevel")
	private Integer lightlevel;
	
	@JsonProperty("lux")
	private Integer lux;
}
