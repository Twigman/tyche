package de.qwyt.housecontrol.tyche.model.sensor.zha;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.LightLevelSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class LightLevelSensor extends Sensor {
	
	@Override
	public LightLevelSensorState getState() {
		return (LightLevelSensorState) super.getState();
	}
}
