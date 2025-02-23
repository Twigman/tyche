package de.qwyt.housecontrol.tyche.event;


import org.springframework.context.ApplicationEvent;

public class RaspbeeWebSocketEvent extends ApplicationEvent {
	
	private Object source;
	
	private String message;
	
	public RaspbeeWebSocketEvent(Object source, String message) {
		super(source);
		
		this.source = source;
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
