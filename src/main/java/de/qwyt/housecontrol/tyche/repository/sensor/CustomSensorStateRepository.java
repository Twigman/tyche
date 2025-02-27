package de.qwyt.housecontrol.tyche.repository.sensor;

import java.time.Instant;
import java.util.List;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;
import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorStatesCollectionName;

public interface CustomSensorStateRepository {

	<T extends SensorState> T saveSensorState(T sensorState);
	
	public SensorState findLatestSensorState(String sensorId, SensorStatesCollectionName collection);
	
	public List<SensorState> findSensorStateBetween(String sensorId, Instant start, Instant end, SensorStatesCollectionName collection);
	
	
}
