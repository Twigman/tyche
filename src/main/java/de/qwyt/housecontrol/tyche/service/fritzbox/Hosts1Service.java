package de.qwyt.housecontrol.tyche.service.fritzbox;

import fritzbox.ws.tr064.GetGenericHostEntryResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;


@WebService(targetNamespace = "urn:dslforum-org:service:Hosts:1")
public interface Hosts1Service {
	
	@WebMethod(operationName = "GetHostNumberOfEntries", action = "urn:dslforum-org:service:Hosts:1#GetHostNumberOfEntries")
	@WebResult(name = "NewHostNumberOfEntries", targetNamespace = "")
	public int getHostNumberOfEntries();
	
	@WebMethod(operationName = "GetGenericHostEntry", action = "urn:dslforum-org:service:Hosts:1#GetGenericHostEntry")
	@WebResult(name = "GetGenericHostEntryResponse", targetNamespace = "urn:dslforum-org:service:Hosts:1")
	public GetGenericHostEntryResponse getGenericHostEntry(@WebParam(name = "NewIndex") int newIndex);

}
