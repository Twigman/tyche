package de.qwyt.housecontrol.tyche.model.sensor.zha;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.DaylightSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DaylightSensor extends Sensor {

	@Override
	public DaylightSensorState getState() {
		return (DaylightSensorState) super.getState();
	}

}
