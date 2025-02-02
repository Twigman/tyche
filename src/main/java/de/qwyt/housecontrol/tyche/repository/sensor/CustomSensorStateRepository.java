package de.qwyt.housecontrol.tyche.repository.sensor;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;

public interface CustomSensorStateRepository {

	<T extends SensorState> T saveSensorState(T sensorState);
	
}
