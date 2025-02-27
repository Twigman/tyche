package de.qwyt.housecontrol.tyche.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
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
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetGenericHostEntryRequest;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetGenericHostEntryResponse;

@Service
public class AutomationServiceImpl {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final LightServiceImpl lightService;
	
	private final RoomServiceImpl roomService;
	
	private final AutomationProfileManager automationProfileManager;
	
	private final TimerService timerService;
	
	private final FritzboxService fritzboxService;
	
	@Value("${tyche.light.timer}")
	private int lightTimer;
	
	@Value("${tyche.phone.mac}")
	private String phoneMac;
	
	@Value("${tyche.phone.entryIndex}")
	private int phoneEntryIndex;
	
	@Autowired
	public AutomationServiceImpl(
			LightServiceImpl lightService,
			RoomServiceImpl roomService,
			AutomationProfileManager automationProfileManager, 
			TimerService timerService,
			FritzboxService fritzboxService
			) {
		this.lightService = lightService;
		this.roomService = roomService;
		this.automationProfileManager = automationProfileManager;
		this.timerService = timerService;
		this.fritzboxService = fritzboxService;
	}
	
	@EventListener
	public void onSensorPresenceEvent(SensorPresenceEvent presenceEvent) {
		PresenceSensor sensor = presenceEvent.getSensor();
		// Is motion detection active?
		if (automationProfileManager.getActiveProfile().getActivateMotionDetection()) {
			// motion detection -> active
			if (roomService.getRoom(RoomType.HALLWAY).getSensorIdList().contains(sensor.getUniqueId()) && sensor.getState().isPresence()) {
				// Hallway
				LOG.debug("Motion Detector HALLWAY: Presence");
				
				// if user was away, check if he is back
				if (automationProfileManager.getActiveProfile().getProfileType() == AutomationProfileType.AWAY) {
					this.checkPhoneConnection();
				}
				
				if (automationProfileManager.getActiveProfile().getActivateLightAutomation()) {
					// light automation -> active
					lightService.turnOnLightsIn(
							roomService.getRoom(RoomType.HALLWAY),
							automationProfileManager.getActiveProfile().getPresets().get(RoomType.HALLWAY).getLights().getColorProfile()
							);
					timerService.startTimer(sensor.getUniqueId(), lightTimer, () -> lightService.turnOffLightsIn(roomService.getRoom(RoomType.HALLWAY)));
				}
			} else if (roomService.getRoom(RoomType.KITCHEN).getSensorIdList().contains(sensor.getUniqueId()) && sensor.getState().isPresence()) {
				LOG.debug("Motion Detector KITCHEN: Presence");
				// Kitchen
				if (automationProfileManager.getActiveProfile().getActivateLightAutomation()) {
					// light automation -> active
					lightService.turnOnLightsIn(
							roomService.getRoom(RoomType.KITCHEN),
							automationProfileManager.getActiveProfile().getPresets().get(RoomType.KITCHEN).getLights().getColorProfile()
							);
					timerService.startTimer(sensor.getUniqueId(), lightTimer, () -> lightService.turnOffLightsIn(roomService.getRoom(RoomType.KITCHEN)));
				}
			}
		} else {
			// motion detection -> deactivated
		}
	}
	
	@EventListener
	public void onDimmerSwitchEvent(DimmerSwitchEvent dimmerEvent) {
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
		// stop all timers
		timerService.cancelAllTimers();
		
		map.forEach((roomType, preset) -> {
			Room room =  this.roomService.getRoom(roomType);
			lightService.updateLightsIn(room, preset.getLights());
		});
	}
	
	@Scheduled(fixedRate = 60000)
	public void checkPhoneConnection() {
		GetGenericHostEntryResponse responsePhone = fritzboxService.getGenericHostEntry(phoneEntryIndex);
		
		if (responsePhone.getNewMACAddress().equals(phoneMac) && responsePhone.isNewActive()) {
			// phone is in network
			LOG.debug("Phone is connected");
			// keep active profile if not AWAY
			if (this.automationProfileManager.getActiveProfile().getProfileType() == AutomationProfileType.AWAY) {
				// user is back
				LOG.info("Phone joined network");
				this.automationProfileManager.setActiveProfile(AutomationProfileType.HOME);
				this.executePreset(this.automationProfileManager.getActiveProfile().getPresets());
			} else {
				// user was already at home
			}
		} else if (responsePhone.getNewMACAddress().equals(phoneMac) && !responsePhone.isNewActive()) {
			LOG.debug("Phone is disconnected");
			// keep active profile if AWAY or VACATION
			if (this.automationProfileManager.getActiveProfile().getProfileType() == AutomationProfileType.AWAY ||
					this.automationProfileManager.getActiveProfile().getProfileType() == AutomationProfileType.VACATION) {
				// user is still away
			} else {
				// user left home
				LOG.info("Phone left network");
				this.automationProfileManager.setActiveProfile(AutomationProfileType.AWAY);
				this.executePreset(this.automationProfileManager.getActiveProfile().getPresets());
			}
		} else {
			// check other entries
		}
	}
}
