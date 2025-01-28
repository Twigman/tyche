package de.qwyt.housecontrol.tyche.model.websocket;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NOT_USED_RaspbeeState {

	private int humidity;
	
	private int pressure;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date lastupdated;
	
	private boolean dark;
	
	private boolean daylight;
	
	private int lightlevel;
	
	private int lux;
}
