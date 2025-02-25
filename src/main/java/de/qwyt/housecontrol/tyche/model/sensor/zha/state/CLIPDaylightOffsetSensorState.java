package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import java.time.Instant;
import java.util.Date;

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
@Document(collection = "CLIPDaylightOffsetSensorStates")
public class CLIPDaylightOffsetSensorState extends SensorState {
	
	@JsonProperty("localtime")
	@JsonDeserialize(using = FlexibleInstantDeserializier.class)
	private Instant localtime;

}
