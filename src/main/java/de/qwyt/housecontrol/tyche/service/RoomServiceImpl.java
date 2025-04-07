package de.qwyt.housecontrol.tyche.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.event.LogLevel;
import de.qwyt.housecontrol.tyche.model.group.Room;
import de.qwyt.housecontrol.tyche.model.group.RoomType;
import de.qwyt.housecontrol.tyche.model.group.RoomVisitProperties;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfilePreset;
import de.qwyt.housecontrol.tyche.repository.group.RoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

/**
 * roomMap is set by application.yml (@ConfigurationProperties)
 * 
 */
@Service
@ConfigurationProperties(prefix = "tyche")
public class RoomServiceImpl {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private Map<RoomType, RoomVisitProperties> mapVisitProperties;
	
	@Getter
	@Setter
	private Map<RoomType, Room> roomMap;
	
	private final RoomRepository roomRepository;
	
	private final EventServiceImpl eventService;
	
	@Autowired
	public RoomServiceImpl(
			RoomRepository roomRepository,
			EventServiceImpl eventService,
			EventServiceImpl eventService2
			) {
		this.roomRepository = roomRepository;
		this.eventService = eventService2;
	}
	
	@PostConstruct
	public void init() {
		mapVisitProperties = new HashMap<>();
	}


	public Map<RoomType, Room> saveRoomsinDb() {
		LOG.debug("{} rooms initialized from config", roomMap.size());
		
		try {
			List<Room> roomsDbList = roomRepository.findAll();
			Map<RoomType, Room> roomsDbMap = roomsDbList.stream()
					.collect(Collectors.toMap(Room::getName, room -> room));
			
			LOG.info("{} rooms loaded from database", roomsDbList.size());
			
			// are initialized and loaded rooms equal?
			Set<RoomType> uniqueKeysInDatabase = roomsDbMap.keySet().stream().filter(key -> !roomMap.containsKey(key)).collect(Collectors.toSet());
			
			// delete not existing rooms 
			// this can happen when lights/sensors break
			// empty existing rooms can cause problems
			if (uniqueKeysInDatabase.size() != 0) {
				uniqueKeysInDatabase.forEach((type) -> {
					LOG.info("Room '{}' no longer exists", type);
					roomRepository.delete(roomsDbMap.get(type));
					LOG.info("{} deleted", type);
				});
			}
			
			this.roomMap.forEach((type, room) -> {
				room.setName(type);
				
				if (roomsDbMap.containsKey(type)) {
					room.setId(roomsDbMap.get(type).getId());
				} else {
					LOG.info("New room {} will be saved in database", room.getName());
				}
	
				roomRepository.save(room);
			});
		} catch (IllegalArgumentException e) {
			// TODO: detect the problematic entry
			// can happen if a room has been removed and the enum type doesn't exist
			LOG.error(e.getMessage());
			roomRepository.deleteAll();
			LOG.warn("Type not available. Deleted all rooms in database to create them again.");
			saveRoomsinDb();
		}

		return this.roomMap;
	}
	
	public Room getRoom(RoomType name) {
		return this.roomMap.get(name);
	}
	
	public void addVisitIn(RoomType type) {
		if (mapVisitProperties.get(type).isWatchVisits()) {
			// activity
			LOG.debug("Added visit in " + type.name());
			mapVisitProperties.get(type).increaseVisitCounterBy(1);
			mapVisitProperties.get(type).getVisitTimestamps().add(Instant.now());
			
			this.checkRoomVisitThreshold(type);
		}
	}
	
	public void updateRoomVisitProperties(Map<RoomType, AutomationProfilePreset> preset) {
		for (RoomType type : RoomType.values()) {
			if (type.equals(RoomType.ALL)) {
				continue;
			}
			
			if (!mapVisitProperties.containsKey(type)) {
				mapVisitProperties.put(type, new RoomVisitProperties());
			}

			mapVisitProperties.get(type).setVisitThreshold(preset.get(type).getAutoProfileSwitch().getVisitThreshold());
			mapVisitProperties.get(type).setVisitCounter(0);
			mapVisitProperties.get(type).setVisitTimespanInSec(preset.get(type).getAutoProfileSwitch().getInTimespanSec());
			mapVisitProperties.get(type).setWatchVisits(preset.get(type).getAutoProfileSwitch().isEnabled());
		}
	}
	
	private void checkRoomVisitThreshold(RoomType type) {
		if (mapVisitProperties.get(type).getVisitThreshold() <= mapVisitProperties.get(type).getVisitCounterForTimespan()) {
			mapVisitProperties.get(type).getVisitTimestamps().clear();
			// switch profile
			LOG.info("Profile switch triggered! Visit threshold reached in " + type.name() + " (" + mapVisitProperties.get(type).getVisitThreshold() + " visits in " + (mapVisitProperties.get(type).getVisitTimespanInSec() / 60) + " min).");

			eventService.fireLogEvent(HousecontrolModule.SYSTEM, LogLevel.INFO, "Profile switch triggered! Visit threshold reached in " + type.name() + " (" + mapVisitProperties.get(type).getVisitThreshold() + " visits in " + (mapVisitProperties.get(type).getVisitTimespanInSec() / 60) + " min).");
			eventService.fireRoomVisitThresholdReachedEvent(type);
		}
	}

}
