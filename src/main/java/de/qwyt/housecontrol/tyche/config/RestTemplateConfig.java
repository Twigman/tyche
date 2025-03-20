package de.qwyt.housecontrol.tyche.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	
	@Value("${meteomatics.username}")
	private String username;
	
	@Value("${meteomatics.pw}")
	private String pw;
	
	@Bean
	public RestTemplate restTemplateWithBasicAuth(RestTemplateBuilder builder) {
		return builder.basicAuthentication(username, pw).build();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
