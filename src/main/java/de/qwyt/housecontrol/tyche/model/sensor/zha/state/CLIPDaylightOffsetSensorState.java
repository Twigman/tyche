package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Document(collection = "CLIPDaylightOffsetSensorState")
public class CLIPDaylightOffsetSensorState extends SensorState {
	
	@JsonProperty("localtime")
	private Date localtime;

}
