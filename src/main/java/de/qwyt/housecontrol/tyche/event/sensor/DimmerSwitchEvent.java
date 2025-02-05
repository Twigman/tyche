package de.qwyt.housecontrol.tyche.event.sensor;

import org.springframework.context.ApplicationEvent;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import lombok.Getter;

@Getter
public class DimmerSwitchEvent extends ApplicationEvent {

	private DimmerSwitch dimmSwitch;
	
	private HousecontrolModule module;
	
	public DimmerSwitchEvent(Object source, HousecontrolModule module, DimmerSwitch dimmerSwitch) {
		super(source);
		this.dimmSwitch = dimmerSwitch;
		this.module = module;
	}
}
