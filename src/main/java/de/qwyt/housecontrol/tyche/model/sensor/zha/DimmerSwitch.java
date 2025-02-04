package de.qwyt.housecontrol.tyche.model.sensor.zha;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.DimmerSwitchState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DimmerSwitch extends Sensor {

	@Override
	public DimmerSwitchState getState() {
		return (DimmerSwitchState) super.getState();
	}
	
}
