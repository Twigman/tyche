package de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064.hosts;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "GetSpecificHostEntryResponse", namespace = "urn:dslforum-org:service:Hosts:1")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetSpecificHostEntryResponse {

    @XmlElement(name = "NewIPAddress")
    private String newIPAddress;

    @XmlElement(name = "NewAddressSource")
    private String newAddressSource;

    @XmlElement(name = "NewLeaseTimeRemaining")
    private int newLeaseTimeRemaining;

    @XmlElement(name = "NewInterfaceType")
    private String newInterfaceType;

    @XmlElement(name = "NewActive")
    private boolean newActive;

    @XmlElement(name = "NewHostName")
    private String newHostName;
	
}