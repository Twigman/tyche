package de.qwyt.housecontrol.tyche.repository.sensor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;


/**
 * Implements a custom method to save a concrete SensorState depending on the Document annotation 
 * 
 */
public class CustomSensorStateRepositoryImpl implements CustomSensorStateRepository {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public <T extends SensorState> T saveSensorState(T sensorState) {
		
		String collectionName = getCollectionName(sensorState.getClass());
		
		// Save sensorState in the specific collection from @Document
		mongoTemplate.save(sensorState, collectionName);
		return sensorState;
	}

	private String getCollectionName(Class<? extends SensorState> c) {
		// Get the collection for the annotation
		Document annotation = c.getAnnotation(Document.class);
		
		if (annotation != null) {
			return annotation.collection();
		}
		// No Document Annotation
		// Fallback-Collection
		return "SensorStates";
	}
}
