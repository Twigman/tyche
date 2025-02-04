package de.qwyt.housecontrol.tyche.repository.light;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;

public interface HueLightStateRepository extends MongoRepository<HueLightState, String>, CustomHueLightStateRepository {

}
