package de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "GetSpecificHostEntry", namespace = "urn:dslforum-org:service:Hosts:1")
@XmlType(name = "", propOrder = {})
@XmlAccessorType(XmlAccessType.FIELD)
public class GetSpecificHostEntryRequest {

	@XmlElement(name = "NewMACAddress", required = true)
    private String newMacAddress;

    public GetSpecificHostEntryRequest() {
    }

    public GetSpecificHostEntryRequest(String newMacAddress) {
        this.newMacAddress = newMacAddress;
    }
}
