package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "CLIPDaylightOffsetSensorState")
public class CLIPDaylightOffsetSensorState extends SensorState {
	
	@JsonProperty("localtime")
	private Date localtime;

}
