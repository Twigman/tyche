package de.qwyt.housecontrol.tyche.model.sensor.zha;


import de.qwyt.housecontrol.tyche.model.sensor.zha.state.PressureSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PressureSensor extends Sensor {

	@Override
	public PressureSensorState getState() {
		return (PressureSensorState) super.getState();
	}
	
}
