package de.qwyt.housecontrol.tyche.repository.sensor;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.qwyt.housecontrol.tyche.model.sensor.zha.GeneralSensorConfig;


public interface SensorConfigRepository extends MongoRepository<GeneralSensorConfig, String> {

}
