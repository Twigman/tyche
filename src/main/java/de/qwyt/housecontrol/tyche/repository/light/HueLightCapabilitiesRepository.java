package de.qwyt.housecontrol.tyche.repository.light;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightCapabilities;

public interface HueLightCapabilitiesRepository extends MongoRepository<HueLightCapabilities, String> {

}
