package de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "GetGenericHostEntry", namespace = "urn:dslforum-org:service:Hosts:1")
@XmlType(name = "", propOrder = {})
@XmlAccessorType(XmlAccessType.FIELD)
public class GetGenericHostEntryRequest {
	
    @XmlElement(name = "NewIndex", required = true)
    private int newIndex;

    public GetGenericHostEntryRequest() {
    }

    public GetGenericHostEntryRequest(int newIndex) {
        this.newIndex = newIndex;
    }
}
