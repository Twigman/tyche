package de.qwyt.housecontrol.tyche.repository.light;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightColorCt;

public interface HueLightColorCtRepository extends MongoRepository<HueLightColorCt, String> {

}
