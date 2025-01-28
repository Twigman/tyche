package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "DaylightSensorState")
public class DaylightSensorState extends SensorState {
	
	@JsonProperty("dark")
	private boolean dark;
	
	@JsonProperty("daylight")
	private boolean daylight;
	
	@JsonProperty("status")
	private boolean status;
	
	@JsonProperty("sunrise")
	private Date sunrise;
	
	@JsonProperty("sunset")
	private Date sunset;
}
