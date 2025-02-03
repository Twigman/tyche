package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.qwyt.housecontrol.tyche.model.deserializer.FlexibleInstantDeserializier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Document(collection = "DaylightSensorStates")
public class DaylightSensorState extends SensorState {
	
	@JsonProperty("dark")
	private boolean dark;
	
	@JsonProperty("daylight")
	private boolean daylight;
	
	@JsonProperty("status")
	private boolean status;
	
	@JsonProperty("sunrise")
	@JsonDeserialize(using = FlexibleInstantDeserializier.class)
	private Instant sunrise;
	
	@JsonProperty("sunset")
	@JsonDeserialize(using = FlexibleInstantDeserializier.class)
	private Instant sunset;
}
