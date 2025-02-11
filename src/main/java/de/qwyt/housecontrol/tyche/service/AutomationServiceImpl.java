package de.qwyt.housecontrol.tyche.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.event.sensor.DimmerSwitchEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.model.group.Room;
import de.qwyt.housecontrol.tyche.model.group.RoomType;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfileType;
import de.qwyt.housecontrol.tyche.model.profile.automation.LightPresets;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfileType;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import de.qwyt.housecontrol.tyche.util.Symbole;

@Service
public class AutomationServiceImpl {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final LightServiceImpl lightService;
	
	private final RoomServiceImpl roomService;
	
	private final AutomationProfileManager automationProfileManager;
	
	@Autowired
	public AutomationServiceImpl(
			LightServiceImpl lightService,
			RoomServiceImpl roomService,
			AutomationProfileManager automationProfileManager
			) {
		this.lightService = lightService;
		this.roomService = roomService;
		this.automationProfileManager = automationProfileManager;
	}
	
	@EventListener
	public void handlePresenceEvent(SensorPresenceEvent presenceEvent) {
		PresenceSensor sensor = presenceEvent.getSensor();
		if (roomService.getRoom(RoomType.HALLWAY).getSensorIdList().contains(sensor.getUniqueId())) {
			// Hallway
			if (sensor.getState().isPresence()) {
				LOG.debug("Motion Detector HALLWAY: Presence");
				lightService.turnOnLightsIn(roomService.getRoom(RoomType.HALLWAY), HueColorProfileType.DEFAULT_CT);
			}	
		} else if (roomService.getRoom(RoomType.KITCHEN).getSensorIdList().contains(sensor.getUniqueId())) {
			// Kitchen
			if (sensor.getState().isPresence()) {
				LOG.debug("Motion Detector KITCHEN: Presence");
				lightService.turnOnLightsIn(roomService.getRoom(RoomType.KITCHEN), HueColorProfileType.DEFAULT_CT);
			}
		}
	}
	
	@EventListener
	public void handleDimmerEvent(DimmerSwitchEvent dimmerEvent) {
		DimmerSwitch dimmerSwitch = dimmerEvent.getDimmSwitch();
		
		switch(dimmerSwitch.getState().getButton()) {
		case BUTTON_ON_PRESSED:
			this.automationProfileManager.setActiveProfile(AutomationProfileType.HOME);
			this.executePreset(this.automationProfileManager.getActiveProfile().getPresets());
			break;
		case BUTTON_OFF_PRESSED:
			this.automationProfileManager.setActiveProfile(AutomationProfileType.BEDTIME);
			this.executePreset(this.automationProfileManager.getActiveProfile().getPresets());
			break;
		case BUTTON_OFF_HOLD:
			if (dimmerSwitch.getState().getEventduration() >= 16) {
				this.automationProfileManager.setActiveProfile(AutomationProfileType.SLEEPING);
				this.executePreset(this.automationProfileManager.getActiveProfile().getPresets());
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
	
	
	private void executePreset(Map<RoomType, LightPresets> map) {
		map.forEach((roomType, preset) -> {
			Room room =  this.roomService.getRoom(roomType);
			lightService.updateLightsIn(room, preset.getLights(), HueColorProfileType.DEFAULT_CT);
		});
	}
}
