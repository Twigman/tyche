package de.qwyt.housecontrol.tyche.model.sensor.zha;


import de.qwyt.housecontrol.tyche.model.sensor.zha.state.PressureSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PressureSensor extends Sensor {

	@Override
	public PressureSensorState getState() {
		return (PressureSensorState) super.getState();
	}
	
}
