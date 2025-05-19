package de.qwyt.housecontrol.tyche.config.settings;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
