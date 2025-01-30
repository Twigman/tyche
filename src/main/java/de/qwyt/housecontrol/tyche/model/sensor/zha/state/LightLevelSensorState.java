package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
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
