package de.qwyt.housecontrol.tyche.model.sensor.zha;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.HumiditySensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class HumiditySensor extends Sensor {
	
	@Override
	public HumiditySensorState getState() {
		return (HumiditySensorState) super.getState();
	}

}
