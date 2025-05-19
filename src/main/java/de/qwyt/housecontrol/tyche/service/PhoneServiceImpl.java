package de.qwyt.housecontrol.tyche.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.model.device.PhoneInfo;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetGenericHostEntryResponse;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetSpecificHostEntryResponse;

@Service
public class PhoneServiceImpl {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final PhoneInfo phoneInfo;
	
	private final EventServiceImpl eventService;
	
	@Autowired
	public PhoneServiceImpl(PhoneInfo phoneInfo, EventServiceImpl eventService) {
		this.phoneInfo = phoneInfo;
		this.eventService = eventService;
	}
	
	public PhoneInfo getPhoneInfo() {
		return this.phoneInfo;
	}

	public boolean updateInfoFromFritzbox(GetGenericHostEntryResponse response) {
		boolean changes = phoneInfo.updateInfoFromFritzbox(response);
		
		if (changes) {
			eventService.firePhoneEvent(phoneInfo);
		}
		
		return changes;
	}
	
	public boolean updateInfoFromFritzbox(GetSpecificHostEntryResponse response) {
		boolean changes = phoneInfo.updateInfoFromFritzbox(response);
		
		if (changes) {
			eventService.firePhoneEvent(phoneInfo);
		}
		
		return changes;
	}
}
