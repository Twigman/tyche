package de.qwyt.housecontrol.tyche.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.service.fritzbox.Hosts1Service;
import fritzbox.ws.tr064.GetGenericHostEntryResponse;
import fritzbox.ws.tr064.GetHostNumberOfEntriesResponse;

@Service
public class FritzboxService {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final Hosts1Service hosts1Service;
	
	@Autowired
	public FritzboxService(Hosts1Service hosts1Service) {
		this.hosts1Service = hosts1Service;
	}
	
	public int getHostNumberOfEntries() {
		return hosts1Service.getHostNumberOfEntries();
	}
	
	public GetGenericHostEntryResponse getGenericHostEntry(int index) {
		return hosts1Service.getGenericHostEntry(index);
	}
	
}
