package de.qwyt.housecontrol.tyche.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.event.sensor.DimmerSwitchEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.state.DimmerSwitchButtonMapping;

@Service
public class AutomationServiceImpl {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final LightServiceImpl lightService;
	
	@Autowired
	public AutomationServiceImpl(LightServiceImpl lightService) {
		this.lightService = lightService;
	}
	
	@EventListener
	public void handlePresenceEvent(SensorPresenceEvent presenceEvent) {
		PresenceSensor sensor = presenceEvent.getSensor();
		
		// test
		if (sensor.getUniqueId().equals("00:17:88:01:02:13:27:22-02-0406")) {
			if (sensor.getState().isPresence()) {
				LOG.debug("Light Flur: ON");
				lightService.turnOnLight("00:17:88:01:03:20:38:fd-0b");
			}
			
		}
	}
	
	@EventListener
	public void handleDimmerEvent(DimmerSwitchEvent dimmerEvent) {
		DimmerSwitch dimmerSwitch = dimmerEvent.getDimmSwitch();
		
		switch(dimmerSwitch.getState().getButton()) {
		case BUTTON_OFF_PRESSED:
			// how long?
			LOG.debug("OFF", dimmerSwitch.getState().getEventduration());
			break;
		case BUTTON_OFF_HOLD:
			LOG.debug("OFF hold for: {}", dimmerSwitch.getState().getEventduration());
			if (dimmerSwitch.getState().getEventduration() >= 16) {
				// turn off all lights
			}
			break;
		case BUTTON_OFF_RELEASED:
			LOG.debug("OFF released");
			break;
		case BUTTON_OFF_HOLD_RELEASED:
			LOG.debug("OFF released after: {}", dimmerSwitch.getState().getEventduration());
			break;
		default:
			LOG.warn("Unknown button: {}", dimmerSwitch.getState().getButton());
			break;
		}
		
		
		
		
	}

}
