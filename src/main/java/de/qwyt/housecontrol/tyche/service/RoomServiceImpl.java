package de.qwyt.housecontrol.tyche.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.model.group.Room;
import de.qwyt.housecontrol.tyche.model.group.RoomType;
import de.qwyt.housecontrol.tyche.repository.group.RoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Service
@ConfigurationProperties(prefix = "tyche")
public class RoomServiceImpl {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Getter
	@Setter
	private Map<RoomType, Room> roomMap;
	
	private final RoomRepository roomRepository;
	
	@Autowired
	public RoomServiceImpl(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
		//this.roomMap = new HashMap<>();
	}


	public Map<RoomType, Room> saveRoomsinDb() {
		List<Room> roomsDbList = roomRepository.findAll();
		
		Map<RoomType, Room> roomsDbMap = roomsDbList.stream()
				.collect(Collectors.toMap(Room::getName, room -> room));
		
		LOG.info("{} rooms loaded from database", roomsDbList.size());
		
		this.roomMap.forEach((type, room) -> {
			room.setName(type);
			
			if (roomsDbMap.containsKey(type)) {
				room.setId(roomsDbMap.get(type).getId());
			} else {
				LOG.info("New room {} will be saved in database", room.getName());
			}

			roomRepository.save(room);
		});
		
		return this.roomMap;
	}
	
	public Room getRoom(RoomType name) {
		return this.roomMap.get(name);
	}
	
	

}
