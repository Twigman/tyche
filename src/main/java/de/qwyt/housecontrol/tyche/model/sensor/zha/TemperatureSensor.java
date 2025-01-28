package de.qwyt.housecontrol.tyche.model.sensor.zha;

import org.springframework.data.mongodb.core.mapping.Document;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.TemperatureSensorState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "TemperatureSensor")
public class TemperatureSensor extends Sensor {
	
	@Override
	public TemperatureSensorState getState() {
		return (TemperatureSensorState) super.getState();
	}

}
