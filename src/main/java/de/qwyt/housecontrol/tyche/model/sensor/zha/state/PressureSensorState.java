package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Document(collection = "PressureSensorState")
public class PressureSensorState extends SensorState {

	@JsonProperty("pressure")
	private Integer pressure;
}
