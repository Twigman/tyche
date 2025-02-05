package de.qwyt.housecontrol.tyche.model.group;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection = "Rooms")
public class Room {
	
	@Id
	private String id;

	@JsonProperty("name")
	private RoomType name;
	
	@JsonProperty("lightIdList")
	private List<String> lightIdList;
	
	@JsonProperty("sensorIdList")
	private List<String> sensorIdList;
	
	public Room() {
		this.lightIdList = new ArrayList<String>();
		this.sensorIdList = new ArrayList<String>();
	}
	
	public void addLightId(String uniqueId) {
		this.lightIdList.add(uniqueId);
	}
	
	public void addSensorId(String uniqueId) {
		this.sensorIdList.add(uniqueId);
	}
}
