package de.qwyt.housecontrol.tyche.model.sensor.zha;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.DaylightSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DaylightSensor extends Sensor {

	@Override
	public DaylightSensorState getState() {
		return (DaylightSensorState) super.getState();
	}

}
