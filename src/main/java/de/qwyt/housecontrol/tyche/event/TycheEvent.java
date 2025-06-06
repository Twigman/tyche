package de.qwyt.housecontrol.tyche.event;

import org.springframework.context.ApplicationEvent;

import de.qwyt.housecontrol.tyche.event.types.HousecontrolModule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class TycheEvent extends ApplicationEvent {

	private static final long serialVersionUID = -3805641093024446051L;
	
	private HousecontrolModule module;
	
	public TycheEvent(Object source, HousecontrolModule module) {
		super(source);
		this.module = module;
	}
}
