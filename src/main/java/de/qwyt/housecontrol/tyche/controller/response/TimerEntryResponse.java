package de.qwyt.housecontrol.tyche.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimerEntryResponse {

	private String id;
	
	private long remainingTime;
	
}
