package de.qwyt.housecontrol.tyche.service;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.event.HousecontrolModule;
import de.qwyt.housecontrol.tyche.event.LogLevel;
import de.qwyt.housecontrol.tyche.event.RoomVisitThresholdReachedEvent;
import de.qwyt.housecontrol.tyche.event.sensor.DimmerSwitchEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.model.group.Room;
import de.qwyt.housecontrol.tyche.model.group.RoomType;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfileType;
import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfilePreset;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetSpecificHostEntryResponse;

@Service
public class AutomationServiceImpl {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final LightServiceImpl lightService;
	
	private final RoomServiceImpl roomService;
	
	private final AutomationProfileManager automationProfileManager;
	
	private final TimerService timerService;
	
	private final PhoneServiceImpl phoneService;
	
	private final FritzboxService fritzboxService;
	
	private final EventServiceImpl eventService;
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	private ScheduledFuture<?> dynamicTask;
	
	private final String motionTimerPrefix = "timer-motion-";
	
	private final String profileTimerPrefix = "timer-profile-";
	
	private boolean checkPhoneAtHighFrequency;
	
	@Value("${tyche.light.timer}")
	private long lightTimer;
	
	@Value("${tyche.profile.autoHomeTimer}")
	private long autoHomeTimer;
	
	@Autowired
	public AutomationServiceImpl(
			LightServiceImpl lightService,
			RoomServiceImpl roomService,
			AutomationProfileManager automationProfileManager, 
			TimerService timerService,
			PhoneServiceImpl phoneService,
			FritzboxService fritzboxService,
			EventServiceImpl eventService
			) {
		this.lightService = lightService;
		this.roomService = roomService;
		this.automationProfileManager = automationProfileManager;
		this.timerService = timerService;
		this.fritzboxService = fritzboxService;
		this.phoneService = phoneService;
		this.eventService = eventService;
		this.checkPhoneAtHighFrequency = false;
	}
	
	/**
	 * Sensor detected motion
	 * 
	 * User at home:
	 * 
	 * User away:
	 * 
	 * - user is back?
	 * 
	 * @param presenceEvent
	 */
	@EventListener
	public void onSensorPresenceEvent(SensorPresenceEvent presenceEvent) {
		PresenceSensor sensor = presenceEvent.getSensor();
		//AutomationProfile activeProfile = automationProfileManager.getActiveProfile();
		// necessary for lambda
		AtomicReference<RoomType> affectedRoom = new AtomicReference<RoomType>(null);
		
		// Is motion detection active?
		if (automationProfileManager.getActiveProfile().isActiveMotionDetection()) {
			// motion detection -> active
			if (roomService.getRoom(RoomType.HALLWAY).getSensorIdList().contains(sensor.getUniqueId()) && sensor.getState().isPresence()) {
				/*
				 * HALLWAY
				 */
				affectedRoom.set(RoomType.HALLWAY);
				
				// if user was away, check if he is back
				if (automationProfileManager.getActiveProfile().getProfileType() == AutomationProfileType.AWAY) {
					this.checkPhoneConnectionBurst();
				}
			} else if (roomService.getRoom(RoomType.KITCHEN).getSensorIdList().contains(sensor.getUniqueId()) && sensor.getState().isPresence()) {
				affectedRoom.set(RoomType.KITCHEN);
			}
			
			if (affectedRoom.get() != null) {
				roomService.addVisitIn(affectedRoom.get());
				LOG.debug("Motion Detector {}: Presence", affectedRoom.get());
				
				// light automation active and lights are controlled by sensors?
				if (automationProfileManager.getActiveProfile().isActiveLightAutomation() &&
						!automationProfileManager.getActiveProfile().getPresets().get(affectedRoom.get()).getLights().isIgnoreSensors()) {
					// light automation -> active
					lightService.turnOnLightsIn(
							roomService.getRoom(affectedRoom.get()),
							automationProfileManager.getActiveProfile().getPresets().get(affectedRoom.get()).getLights().getColorProfile()
							);
					timerService.startTimer(motionTimerPrefix + sensor.getUniqueId(), lightTimer, () -> lightService.turnOffLightsIn(roomService.getRoom(affectedRoom.get())));
				}
				
				// TODO: works only for cooking profile
				if (automationProfileManager.getActiveProfile().isAutoHomeProfile() && affectedRoom.get() == RoomType.KITCHEN) {
					LOG.debug("Auto HOME timer set back to {} minutes", autoHomeTimer / 60000);
					timerService.startTimer(profileTimerPrefix + automationProfileManager.getActiveProfile().getProfileType().toString() + "-AutoHomeProfileSwitch", autoHomeTimer, () -> automationProfileManager.setActiveProfile(HousecontrolModule.SYSTEM, AutomationProfileType.HOME));
					timerService.startTimer(profileTimerPrefix + automationProfileManager.getActiveProfile().getProfileType().toString() + "-AutoHomeExecPreset", autoHomeTimer, () -> executePreset(automationProfileManager.getActiveProfile().getPresets()));
				}
				
				if (automationProfileManager.getActiveProfile().isActiveLightAutomation() &&
						automationProfileManager.getActiveProfile().getPresets().get(affectedRoom.get()).getLights().getIgnoreSensors()) {
					LOG.debug("While Profile {} is active, lights in {} ignoring sensors", automationProfileManager.getActiveProfile().getProfileType(), affectedRoom.get());
				}
			}
		} else {
			// motion detection -> deactivated
		}
	}
	
	@EventListener
	public void onDimmerSwitchEvent(DimmerSwitchEvent dimmerEvent) {
		DimmerSwitch dimmerSwitch = dimmerEvent.getSensor();
		
		switch(dimmerSwitch.getState().getButton()) {
		case BUTTON_ON_PRESSED:
			this.automationProfileManager.setActiveProfile(HousecontrolModule.MANUAL, AutomationProfileType.HOME);
			this.executePreset(this.automationProfileManager.getActiveProfile().getPresets());
			break;
		case BUTTON_OFF_PRESSED:
			this.automationProfileManager.setActiveProfile(HousecontrolModule.MANUAL, AutomationProfileType.BEDTIME);
			this.executePreset(this.automationProfileManager.getActiveProfile().getPresets());
			break;
		case BUTTON_OFF_HOLD:
			if (dimmerSwitch.getState().getEventduration() >= 16) {
				this.automationProfileManager.setActiveProfile(HousecontrolModule.MANUAL, AutomationProfileType.SLEEPING);
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
	
	@EventListener
	public void onRoomVisitThresholdReached(RoomVisitThresholdReachedEvent event) {
		setAndExecuteActiveProfile(event.getModule(), automationProfileManager.getActiveProfile().getPresets().get(event.getRoomType()).getAutoProfileSwitch().getToProfile());
	}
	
	public boolean setAndExecuteActiveProfile(HousecontrolModule module, AutomationProfileType type) {
		boolean success = automationProfileManager.setActiveProfile(module, type);
		
		if (success) {
			executePreset(automationProfileManager.getActiveProfile().getPresets());
			
			if (automationProfileManager.getActiveProfile().isAutoHomeProfile()) {
				timerService.startTimer(profileTimerPrefix + automationProfileManager.getActiveProfileType().toString() + "-AutoHomeProfileSwitch", autoHomeTimer, () -> automationProfileManager.setActiveProfile(HousecontrolModule.SYSTEM, AutomationProfileType.HOME));
				timerService.startTimer(profileTimerPrefix + automationProfileManager.getActiveProfileType().toString() + "-AutoHomeExecPreset", autoHomeTimer, () -> executePreset(automationProfileManager.getActiveProfile().getPresets()));
				
				try {
					// Event appears before fireAutomationActiveProfile in web log
					Thread.sleep(200);
					eventService.fireLogEvent(HousecontrolModule.SYSTEM, LogLevel.INFO, "Auto HOME is enabled. Switching back to HOME after " + (autoHomeTimer / 60000) + " minutes of inactivity");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return success;
	}
	
	public void executePreset(Map<RoomType, AutomationProfilePreset> map) {
		// stop all timers
		timerService.cancelAllTimers();
		
		map.forEach((roomType, preset) -> {
			Room room =  this.roomService.getRoom(roomType);
			lightService.updateLightsIn(room, preset.getLights());
		});
	}
	
	@Scheduled(fixedRate = 60000)
	public void scheduledCheckPhoneConnection() {
		// do not execute if other method is already checking at high frequency
		if (!checkPhoneAtHighFrequency) {
			GetSpecificHostEntryResponse response = fritzboxService.getPhoneInfo(phoneService.getPhoneInfo().getPhoneSecureInfo().getMacAddress());
			
			if (response == null) {
				LOG.error("Phone entry not found in fritzbox host list!");
				eventService.fireLogEvent(HousecontrolModule.FRITZBOX, LogLevel.ERROR, "Phone entry not found in fritzbox host list!");
			}
			
			phoneService.updateInfoFromFritzbox(response);
			
			if (phoneService.getPhoneInfo().isInHomeWlan()) {
				// phone is in network
				LOG.debug("Phone is connected to home WLAN");
				
				if (this.automationProfileManager.getActiveProfile().getProfileType() == AutomationProfileType.AWAY) {
					// user is back
					this.automationProfileManager.setActiveProfile(HousecontrolModule.PHONE, AutomationProfileType.HOME);
					this.executePreset(this.automationProfileManager.getActiveProfile().getPresets());
				} else {
					// user was already at home
				}
			} else if (!phoneService.getPhoneInfo().isInHomeWlan()) {
				LOG.debug("Phone is disconnected from home WLAN");
				// keep active profile if AWAY or VACATION
				if (this.automationProfileManager.getActiveProfile().getProfileType() == AutomationProfileType.AWAY ||
						this.automationProfileManager.getActiveProfile().getProfileType() == AutomationProfileType.VACATION) {
					// user is still away
				} else {
					// user left home
					LOG.info("Phone left home WLAN");
					this.automationProfileManager.setActiveProfile(HousecontrolModule.PHONE, AutomationProfileType.AWAY);
					this.executePreset(this.automationProfileManager.getActiveProfile().getPresets());
				}
			}
		}
	}
	
	public void checkPhoneConnectionBurst() {
		int durationInSec = 120;
		int intervalInSec = 2;
		
		if (dynamicTask != null && !dynamicTask.isCancelled()) {
			// task is already running
			LOG.info("Phone connection check already in progress");
			eventService.fireLogEvent(HousecontrolModule.SYSTEM, LogLevel.INFO, "Phone connection check already in progress");
		} else {
			LOG.info("Checking phone connection at high frequency! (For {} min every {} s)", (durationInSec / 60), intervalInSec);
			eventService.fireLogEvent(HousecontrolModule.SYSTEM, LogLevel.INFO, "Checking phone connection at high frequency! (For " + (durationInSec / 60) + " min every " + intervalInSec + " s)");
			
			checkPhoneAtHighFrequency = true;
			
			// start new Task
			dynamicTask = scheduler.scheduleAtFixedRate(this::checkPhoneConnection, 0, intervalInSec, TimeUnit.SECONDS);
			
			scheduler.schedule(() -> {
				if (checkPhoneAtHighFrequency && !dynamicTask.isCancelled()) {
					LOG.info("Phone check frequency set back to normal interval (timeout)");
					eventService.fireLogEvent(HousecontrolModule.SYSTEM, LogLevel.INFO, "Phone is not connected to home WLAN. Phone check frequency set back to normal interval.");
					dynamicTask.cancel(false);
					checkPhoneAtHighFrequency = false;
				} else {
					// phone already detected
				}
			}, durationInSec, TimeUnit.SECONDS);
		}
	}
	
	public void checkPhoneConnection() {
		GetSpecificHostEntryResponse response = fritzboxService.getPhoneInfo(phoneService.getPhoneInfo().getPhoneSecureInfo().getMacAddress());
		
		if (response != null) {
			phoneService.updateInfoFromFritzbox(response);
			
			if (phoneService.getPhoneInfo().isInHomeWlan()) {
				// phone is connected to wlan
				eventService.fireLogEvent(HousecontrolModule.SYSTEM, LogLevel.INFO, "Phone is connected to home WLAN");
				this.stopCheckPhoneBurst();
				this.scheduledCheckPhoneConnection();
			}
		} else {
			// Problems with fritzbox
			stopCheckPhoneBurst();
		}
	}
	
	private void stopCheckPhoneBurst() {
		if (dynamicTask != null && !dynamicTask.isCancelled()) {
            dynamicTask.cancel(false);
        }
        checkPhoneAtHighFrequency = false;
        LOG.info("Phone check frequency set back to normal interval (phone detected)");
	}
}
