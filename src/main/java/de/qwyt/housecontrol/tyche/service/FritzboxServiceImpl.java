package de.qwyt.housecontrol.tyche.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetGenericHostEntryRequest;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetGenericHostEntryResponse;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetHostNumberOfEntriesRequest;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetHostNumberOfEntriesResponse;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetSpecificHostEntryRequest;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetSpecificHostEntryResponse;



@Service
public class FritzboxServiceImpl {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final WebServiceTemplate webServiceTemplate;
	
	@Value("${fritzbox.tr064.endpoints.hosts}")
	private String hostsEnpoint;
	
	private List<GetGenericHostEntryResponse> hostList;
	
	@Autowired
	public FritzboxServiceImpl(
			WebServiceTemplate webServiceTemplate
			) {
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
	
	public GetSpecificHostEntryResponse getSpecificHostEntry(String macAddress) {
		GetSpecificHostEntryRequest request = new GetSpecificHostEntryRequest(macAddress);
		GetSpecificHostEntryResponse response = (GetSpecificHostEntryResponse) webServiceTemplate.marshalSendAndReceive(
				hostsEnpoint,
				request,
				new SoapActionCallback("urn:dslforum-org:service:Hosts:1#GetSpecificHostEntry")
				);
		
		return response;
	}
	
	public GetSpecificHostEntryResponse getPhoneInfo(String phoneMac) {
		GetSpecificHostEntryResponse responsePhone = this.getSpecificHostEntry(phoneMac);
		
		if (responsePhone != null) {
			// right entry
			return responsePhone;
		} else {
			// TODO: check other entries
			LOG.warn("Phone MAC address not found in host list");
			return null;
		}
	}
	
	public void initHostList() {
		
	}
}
