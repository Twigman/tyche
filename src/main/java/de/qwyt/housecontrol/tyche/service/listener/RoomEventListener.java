package de.qwyt.housecontrol.tyche.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import de.qwyt.housecontrol.tyche.event.RoomVisitThresholdReachedEvent;
import de.qwyt.housecontrol.tyche.service.AutomationServiceImpl;

@Component
public class RoomEventListener {
	
	private final AutomationServiceImpl automationService;
	
	@Autowired
	public RoomEventListener(AutomationServiceImpl automationService) {
		this.automationService = automationService;
	}

	@EventListener
	public void onRoomVisitThresholdReached(RoomVisitThresholdReachedEvent event) {
		automationService.handleAutoProfileSwitchForRoom(event.getRoomType(), event.getModule());
	}
}
