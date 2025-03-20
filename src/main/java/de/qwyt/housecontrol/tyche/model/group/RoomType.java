package de.qwyt.housecontrol.tyche.model.group;

/**
 * Defines all available rooms.
 * 
 * Add a room:
 * - enter the name of it as a type here
 * - assign device ids in the application.yml to the new room
 * => 	after that the room can be used in the automation-profiles.yml and is included in the preset 'ALL'. 
 * 		It also will be stored in the database.
 */
public enum RoomType {
	LIVINGROOM,
	KITCHEN,
	//BEDROOM,
	HALLWAY,
	BATHROOM,
	ALL
}
