package de.qwyt.housecontrol.tyche.repository.sensor;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;

public interface SensorStateRepository extends MongoRepository<SensorState, String> {

}
