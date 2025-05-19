package de.qwyt.housecontrol.tyche.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import de.qwyt.housecontrol.tyche.event.sensor.DimmerSwitchEvent;
import de.qwyt.housecontrol.tyche.event.sensor.SensorPresenceEvent;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DimmerSwitch;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import de.qwyt.housecontrol.tyche.service.AutomationServiceImpl;

@Component
public class SensorEventListener {
	
	private final AutomationServiceImpl automationService;
	
	@Autowired
	public SensorEventListener(AutomationServiceImpl automationService) {
		this.automationService = automationService;
	}

	@EventListener
	public void onSensorPresenceEvent(SensorPresenceEvent presenceEvent) {
		PresenceSensor sensor = presenceEvent.getSensor();
		automationService.processSensorPresenceInput(sensor);
	}
	
	@EventListener
	public void onDimmerSwitchEvent(DimmerSwitchEvent dimmerEvent) {
		DimmerSwitch dimmerSwitch = dimmerEvent.getSensor();
		automationService.applyDimmerSwitchAction(dimmerSwitch.getState());
	}
}
