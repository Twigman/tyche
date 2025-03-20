package de.qwyt.housecontrol.tyche.event;


import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaspbeeWebSocketEvent extends ApplicationEvent {
	
	private Object source;
	
	private String message;
	
	public RaspbeeWebSocketEvent(Object source, String message) {
		super(source);
		
		this.source = source;
		this.message = message;
	}
}
