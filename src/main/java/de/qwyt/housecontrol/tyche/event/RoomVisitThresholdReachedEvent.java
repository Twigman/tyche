package de.qwyt.housecontrol.tyche.event;

import de.qwyt.housecontrol.tyche.event.types.HousecontrolModule;
import de.qwyt.housecontrol.tyche.model.group.RoomType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomVisitThresholdReachedEvent extends TycheEvent {

	private static final long serialVersionUID = -1490592531307050662L;
	
	private RoomType roomType;
	
	public RoomVisitThresholdReachedEvent(Object source, HousecontrolModule module, RoomType roomType) {
		super(source, module);
		this.roomType = roomType;
	}

}
