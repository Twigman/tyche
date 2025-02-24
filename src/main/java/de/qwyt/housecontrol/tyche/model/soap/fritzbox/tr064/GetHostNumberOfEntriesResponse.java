package de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetHostNumberOfEntriesResponse", namespace = "urn:dslforum-org:service:Hosts:1")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetHostNumberOfEntriesResponse {

    @XmlElement(name = "NewHostNumberOfEntries")
    private int newHostNumberOfEntries;

    public int getNewHostNumberOfEntries() {
        return newHostNumberOfEntries;
    }

    public void setNewHostNumberOfEntries(int newHostNumberOfEntries) {
        this.newHostNumberOfEntries = newHostNumberOfEntries;
    }
}