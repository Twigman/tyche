package de.qwyt.housecontrol.tyche.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.qwyt.housecontrol.tyche.model.sensor.zha.TemperatureSensor;

public interface TemperatureRepository extends MongoRepository<TemperatureSensor, String> {

}
