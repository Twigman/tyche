package de.qwyt.housecontrol.tyche.repository.sensor;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;


public interface SensorRepository extends MongoRepository<Sensor, String> {

}
