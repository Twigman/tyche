package de.qwyt.housecontrol.tyche.model.sensor.zha;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.TemperatureSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class TemperatureSensor extends Sensor {
	
	@Override
	public TemperatureSensorState getState() {
		return (TemperatureSensorState) super.getState();
	}

}
