package de.qwyt.housecontrol.tyche.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogEvent extends TycheEvent {
	
	private static final long serialVersionUID = -1815498789251204030L;
	
	private LogLevel level;
	
	private String message;

	public LogEvent(Object source, HousecontrolModule module, LogLevel level, String message) {
		super(source, module);
		this.level = level;
		this.message = message;
	}
}
