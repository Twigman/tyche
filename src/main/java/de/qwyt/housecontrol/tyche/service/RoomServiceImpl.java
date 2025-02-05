package de.qwyt.housecontrol.tyche.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.model.group.Room;
import de.qwyt.housecontrol.tyche.model.group.RoomType;
import de.qwyt.housecontrol.tyche.repository.group.RoomRepository;

@Service
public class RoomServiceImpl {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private Map<RoomType, Room> roomMap;
	
	private final RoomRepository roomRepository;
	
	@Autowired
	public RoomServiceImpl(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
		this.roomMap = new HashMap<>();
	}


	public Map<RoomType, Room> loadRoomsFromDb() {
		List<Room> rooms = roomRepository.findAll();
		
		this.roomMap = rooms.stream()
				.collect(Collectors.toMap(Room::getName, room -> room));
		
		LOG.info("{} rooms loaded from database", this.roomMap.size());
		
		return this.roomMap;
	}
	
	public Room getRoom(RoomType name) {
		return this.roomMap.get(name);
	}

}
