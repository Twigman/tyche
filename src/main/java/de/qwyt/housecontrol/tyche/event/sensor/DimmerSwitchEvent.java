package de.qwyt.housecontrol.tyche.event.sensor;

import org.springframework.context.ApplicationEvent;

import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import lombok.Getter;

@Getter
public class DimmerSwitchEvent extends ApplicationEvent {

	private DimmerSwitch dimmSwitch;
	
	public DimmerSwitchEvent(Object source, DimmerSwitch dimmerSwitch) {
		super(source);
		this.dimmSwitch = dimmerSwitch;
	}
}
