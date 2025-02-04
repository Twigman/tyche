package de.qwyt.housecontrol.tyche.repository.light;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;

public class CustomHueLightStateRepositoryImpl implements CustomHueLightStateRepository {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public <T extends HueLightState> T saveNew(T hueLightState) {
		// reset Id to generate a new entry in database
		hueLightState.setId(null);
		mongoTemplate.save(hueLightState);
		
		return hueLightState;
	}
}
