package de.qwyt.housecontrol.tyche.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.GetGenericHostEntryRequest;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.GetGenericHostEntryResponse;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.GetHostNumberOfEntriesRequest;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.GetHostNumberOfEntriesResponse;



@Service
public class FritzboxService {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final WebServiceTemplate webServiceTemplate;
	
	@Value("${fritzbox.tr064.endpoints.hosts}")
	private String hostsEnpoint;
	
	@Autowired
	public FritzboxService(WebServiceTemplate webServiceTemplate) {
		this.webServiceTemplate = webServiceTemplate;
	}
	
	public GetHostNumberOfEntriesResponse getHostNumberOfEntries() {
		GetHostNumberOfEntriesRequest request = new GetHostNumberOfEntriesRequest();
		GetHostNumberOfEntriesResponse response = (GetHostNumberOfEntriesResponse) webServiceTemplate.marshalSendAndReceive(
				hostsEnpoint,
				request, 
				new SoapActionCallback("urn:dslforum-org:service:Hosts:1#GetHostNumberOfEntries")
				);
		
		return response;
	}
	
	public GetGenericHostEntryResponse getGenericHostEntry(int index) {
		GetGenericHostEntryRequest request = new GetGenericHostEntryRequest(index);
		GetGenericHostEntryResponse response = (GetGenericHostEntryResponse) webServiceTemplate.marshalSendAndReceive(
				hostsEnpoint,
				request,
				new SoapActionCallback("urn:dslforum-org:service:Hosts:1#GetGenericHostEntry")
				);
		
		return response;
	}
	
}
