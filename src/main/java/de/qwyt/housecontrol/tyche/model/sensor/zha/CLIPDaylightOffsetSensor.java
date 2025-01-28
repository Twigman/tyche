package de.qwyt.housecontrol.tyche.model.sensor.zha;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.CLIPDaylightOffsetSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CLIPDaylightOffsetSensor extends Sensor {
	
	@Override
	public CLIPDaylightOffsetSensorState getState() {
		return (CLIPDaylightOffsetSensorState) super.getState();
	}

}
