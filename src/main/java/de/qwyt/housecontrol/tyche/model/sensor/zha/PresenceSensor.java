package de.qwyt.housecontrol.tyche.model.sensor.zha;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.PresenceSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PresenceSensor extends Sensor {
	
	@Override
	public PresenceSensorState getState() {
		return (PresenceSensorState) super.getState();
	}
}
