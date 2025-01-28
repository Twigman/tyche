package de.qwyt.housecontrol.tyche.model.sensor.zha;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.PresenceSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PresenceSensor extends Sensor {
	
	@Override
	public PresenceSensorState getState() {
		return (PresenceSensorState) super.getState();
	}
}
