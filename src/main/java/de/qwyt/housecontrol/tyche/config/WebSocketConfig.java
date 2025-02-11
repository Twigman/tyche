package de.qwyt.housecontrol.tyche.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import de.qwyt.housecontrol.tyche.service.websocket.adapter.SpringBasicWebSocketAdapter;

import org.springframework.web.socket.client.WebSocketClient;

@Configuration
public class WebSocketConfig {
	
	@Bean
	public SpringBasicWebSocketAdapter SpringBasicWebSocketAdapter() {
		return new SpringBasicWebSocketAdapter();
	}
	
	@Bean
	public WebSocketClient springWebSocketClient() {
		return new StandardWebSocketClient();
	}

}
