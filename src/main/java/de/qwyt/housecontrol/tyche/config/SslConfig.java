package de.qwyt.housecontrol.tyche.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import jakarta.annotation.PostConstruct;

@Configuration
public class SslConfig {

	@Value("${security.truststore.file}")
	private Resource truststore;
	
	@Value("${security.truststore.pw}")
	private String truststorePw;
	
	@PostConstruct
	public void init() {
		try {
			System.setProperty("javax.net.ssl.trustStore", truststore.getFile().getAbsolutePath());
			System.setProperty("javax.net.ssl.trustStorePassword", truststorePw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
