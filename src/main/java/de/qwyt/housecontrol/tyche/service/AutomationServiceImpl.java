package de.qwyt.housecontrol.tyche.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.event.sensor.DimmerSwitchEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.model.group.RoomType;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import de.qwyt.housecontrol.tyche.util.Symbole;

@Service
public class AutomationServiceImpl {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final LightServiceImpl lightService;
	
	private final RoomServiceImpl roomService;
	
	private HomeMode homeMode;
	
	private ActivityMode activityMode;
	
	@Autowired
	public AutomationServiceImpl(LightServiceImpl lightService, RoomServiceImpl roomService) {
		this.lightService = lightService;
		this.roomService = roomService;
		this.homeMode = HomeMode.HOME;
		this.activityMode = ActivityMode.ACTIVE;
	}
	
	@EventListener
	public void handlePresenceEvent(SensorPresenceEvent presenceEvent) {
		PresenceSensor sensor = presenceEvent.getSensor();
		// TODO
		// test
		if (roomService.getRoom(RoomType.HALLWAY).getSensorIdList().contains(sensor.getUniqueId())) {
			// Flur
			if (sensor.getState().isPresence()) {
				LOG.debug("Light Flur: ON");
				lightService.turnOnLightsIn(roomService.getRoom(RoomType.HALLWAY));
			}	
		} else if (roomService.getRoom(RoomType.KITCHEN).getSensorIdList().contains(sensor.getUniqueId())) {
			// Küche
			if (sensor.getState().isPresence()) {
				LOG.debug("Light Küche: ON");
				lightService.turnOnLightsIn(roomService.getRoom(RoomType.KITCHEN));
			}
		}
	}
	
	@EventListener
	public void handleDimmerEvent(DimmerSwitchEvent dimmerEvent) {
		DimmerSwitch dimmerSwitch = dimmerEvent.getDimmSwitch();
		
		switch(dimmerSwitch.getState().getButton()) {
		case BUTTON_ON_PRESSED:
			this.setActivityMode(ActivityMode.ACTIVE, dimmerEvent.getModule());
			break;
		case BUTTON_OFF_PRESSED:
			this.setActivityMode(ActivityMode.BEDTIME, dimmerEvent.getModule());
			// on
			lightService.turnOnLightsIn(roomService.getRoom(RoomType.BEDROOM));
			// off
			lightService.turnOffLightsIn(roomService.getRoom(RoomType.BATHROOM));
			lightService.turnOffLightsIn(roomService.getRoom(RoomType.HALLWAY));
			lightService.turnOffLightsIn(roomService.getRoom(RoomType.KITCHEN));
			lightService.turnOffLightsIn(roomService.getRoom(RoomType.LIVINGROOM));
			break;
		case BUTTON_OFF_HOLD:
			if (dimmerSwitch.getState().getEventduration() >= 16) {
				this.setActivityMode(ActivityMode.SLEEPING, dimmerEvent.getModule());
				LOG.info("{} { all_lights } {} OFF", dimmerEvent.getModule().formatForLog(), Symbole.ARROW_RIGHT);
				// turn off all lights
				lightService.turnOffLightsIn(roomService.getRoom(RoomType.BATHROOM));
				lightService.turnOffLightsIn(roomService.getRoom(RoomType.BEDROOM));
				lightService.turnOffLightsIn(roomService.getRoom(RoomType.HALLWAY));
				lightService.turnOffLightsIn(roomService.getRoom(RoomType.KITCHEN));
				lightService.turnOffLightsIn(roomService.getRoom(RoomType.LIVINGROOM));
				LOG.info("Good night");
			}
			break;
		case BUTTON_OFF_RELEASED:
			//LOG.debug("OFF released");
			break;
		case BUTTON_OFF_HOLD_RELEASED:
			//LOG.debug("OFF released after: {}", dimmerSwitch.getState().getEventduration());
			break;
		default:
			LOG.warn("Unknown button: {}", dimmerSwitch.getState().getButton());
			break;
		}
	}
	
	private HomeMode setHomeMode(HomeMode mode, HousecontrolModule module) {
		if (this.homeMode == mode) {
			return this.homeMode;
		} else {
			// update mode
			this.homeMode = mode;
			LOG.info("{} HOME MODE {} {}", module.formatForLog(), Symbole.ARROW_RIGHT, this.homeMode);
			return this.homeMode;
		}
	}

	private ActivityMode setActivityMode(ActivityMode mode, HousecontrolModule module) {
		if (this.activityMode == mode) {
			return this.activityMode;
		} else {
			// update mode
			this.activityMode = mode;
			LOG.info("{} ACTIVITY MODE {} {}", module.formatForLog(), Symbole.ARROW_RIGHT, this.activityMode);
			return this.activityMode;
		}
	}
}
