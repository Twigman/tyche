package de.qwyt.housecontrol.tyche.event.sensor;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DimmerSwitchEvent extends SensorEvent {

	private static final long serialVersionUID = -7606192762088388894L;
	
	private DimmerSwitch dimmSwitch;
	
	public DimmerSwitchEvent(Object source, HousecontrolModule module, DimmerSwitch dimmerSwitch) {
		super(source, module);
		this.dimmSwitch = dimmerSwitch;
	}
}
