package de.qwyt.housecontrol.tyche.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimerRequest {

	private String label;
	
	private String type;
	
	private String delayString;
	
	private String message;
	
	private TimerAction action;
	
}
