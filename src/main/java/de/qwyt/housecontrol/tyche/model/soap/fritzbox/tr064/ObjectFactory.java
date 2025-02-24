package de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064;

import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetGenericHostEntryRequest;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetGenericHostEntryResponse;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetHostNumberOfEntriesRequest;
import de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts.GetHostNumberOfEntriesResponse;
import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {}

    public GetHostNumberOfEntriesRequest createGetHostNumberOfEntriesRequest() {
        return new GetHostNumberOfEntriesRequest();
    }

    public GetHostNumberOfEntriesResponse createGetHostNumberOfEntriesResponse() {
        return new GetHostNumberOfEntriesResponse();
    }
    
    public GetGenericHostEntryRequest createGetGenericHostEntryRequest(int index) {
        return new GetGenericHostEntryRequest(index);
    }

    public GetGenericHostEntryResponse createGetGenericHostEntryResponse() {
        return new GetGenericHostEntryResponse();
    }
}