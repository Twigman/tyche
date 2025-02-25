package de.qwyt.housecontrol.tyche.repository.sensor;


import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;
import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorStatesCollection;


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
	
	// Between
	public List<SensorState> findSensorStateBetween(String sensorId, Instant start, Instant end, SensorStatesCollection collection) {
		Query query = new Query()
				.addCriteria(Criteria.where("sensorId").is(sensorId))
				.addCriteria(Criteria.where("lastupdated").gte(Date.from(start)).lte(Date.from(end)))
				.with(Sort.by(Sort.Direction.ASC, "lastupdated"));
		
		return mongoTemplate.find(query, SensorState.class, collection.getCollectionName());
	}
	
	// Latest
	public SensorState findLatestSensorState(String sensorId, SensorStatesCollection collection) {
		Query query = new Query()
				.addCriteria(Criteria.where("sensorId").is(sensorId))
				.with(Sort.by(Sort.Direction.DESC, "lastupdated"))
				.limit(1);
		
		return mongoTemplate.findOne(query, SensorState.class, collection.getCollectionName());
	}
}
