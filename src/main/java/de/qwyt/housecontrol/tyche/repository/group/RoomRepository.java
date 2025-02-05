package de.qwyt.housecontrol.tyche.repository.group;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.qwyt.housecontrol.tyche.model.group.Room;

public interface RoomRepository extends MongoRepository<Room, String> {

}
