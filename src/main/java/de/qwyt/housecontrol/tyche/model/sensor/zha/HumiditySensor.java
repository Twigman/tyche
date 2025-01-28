package de.qwyt.housecontrol.tyche.model.sensor.zha;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.HumiditySensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HumiditySensor extends Sensor {
	
	@Override
	public HumiditySensorState getState() {
		return (HumiditySensorState) super.getState();
	}

}
