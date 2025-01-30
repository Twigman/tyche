package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class PressureSensorState extends SensorState {

	@JsonProperty("pressure")
	private Integer pressure;
}
