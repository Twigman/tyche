package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class CLIPDaylightOffsetSensorState extends SensorState {
	
	@JsonProperty("localtime")
	private Date localtime;

}
