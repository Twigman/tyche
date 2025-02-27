package de.qwyt.housecontrol.tyche.event.sensor;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import de.qwyt.housecontrol.tyche.model.sensor.zha.state.LightLevelSensorState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DimmerSwitchEvent extends SensorEvent {

	private static final long serialVersionUID = -7606192762088388894L;
	
	public DimmerSwitchEvent(Object source, HousecontrolModule module, DimmerSwitch dimmerSwitch) {
		super(source, module, dimmerSwitch);
	}
	
	@Override
	public DimmerSwitch getSensor() {
		return (DimmerSwitch) super.getSensor();
	}
}
