package de.qwyt.housecontrol.tyche.config;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import jakarta.annotation.PostConstruct;

@Configuration
public class SslConfig {

	@Value("${security.truststore.file}")
	private String truststorePath;
	
	@Value("${security.truststore.pw}")
	private String truststorePw;
	
	@PostConstruct
	public void init() {
		File truststoreFile = new File(truststorePath);
		
		if (truststoreFile.exists()) {
			System.out.println("Path to truststore.jks: " + truststoreFile.getAbsolutePath());
			
			System.setProperty("javax.net.ssl.trustStore", truststoreFile.getAbsolutePath());
			System.setProperty("javax.net.ssl.trustStorePassword", truststorePw);
		} else {
			System.out.println("Truststore not found at " + truststorePath);
		}

	}
}
