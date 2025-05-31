package de.qwyt.housecontrol.tyche.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.qwyt.housecontrol.tyche.model.device.PhoneInfo;
import de.qwyt.housecontrol.tyche.model.device.PhoneSecureInfo;

@Configuration
public class PhoneConfig {

	@Value("${tyche.phone.mac}")
	private String phoneMac;
	
	@Bean
	public PhoneInfo createPhoneInfo() {
		PhoneInfo phoneInfo = new PhoneInfo();
		PhoneSecureInfo secureInfo = new PhoneSecureInfo();
		
		secureInfo.setMacAddress(phoneMac);
		secureInfo.setIpAddress("");
		
		phoneInfo.setPhoneSecureInfo(secureInfo);
		phoneInfo.setName("");
		phoneInfo.setInHomeWlan(false);
		
		return phoneInfo;
	}
	
}
