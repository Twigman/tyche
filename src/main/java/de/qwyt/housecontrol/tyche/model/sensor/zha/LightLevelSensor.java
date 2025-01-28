package de.qwyt.housecontrol.tyche.model.sensor.zha;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.LightLevelSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LightLevelSensor extends Sensor {
	
	@Override
	public LightLevelSensorState getState() {
		return (LightLevelSensorState) super.getState();
	}
}
