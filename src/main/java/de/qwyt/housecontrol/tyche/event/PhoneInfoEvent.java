package de.qwyt.housecontrol.tyche.event;

import de.qwyt.housecontrol.tyche.model.device.PhoneInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneInfoEvent extends TycheEvent  {

	private static final long serialVersionUID = 390757712564059928L;
	
	private PhoneInfo phoneInfo;
	
	public PhoneInfoEvent(Object source, HousecontrolModule module, PhoneInfo phoneInfo) {
		super(source, module);
		this.phoneInfo = phoneInfo;
	}
}
