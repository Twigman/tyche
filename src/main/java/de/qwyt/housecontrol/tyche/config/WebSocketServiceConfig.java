package de.qwyt.housecontrol.tyche.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketClient;

import de.qwyt.housecontrol.tyche.service.websocket.JettyWebSocketServiceImpl;
import de.qwyt.housecontrol.tyche.service.websocket.SpringBasicWebSocketServiceImpl;
import de.qwyt.housecontrol.tyche.service.websocket.WebSocketService;
import de.qwyt.housecontrol.tyche.service.websocket.adapter.SpringBasicWebSocketAdapter;

@Configuration
public class WebSocketServiceConfig {
	
	@Bean
	public WebSocketService springBasicWebSocketService(WebSocketClient webSocketClient, SpringBasicWebSocketAdapter sessionAdapter) {
		return new SpringBasicWebSocketServiceImpl(webSocketClient, sessionAdapter);
	}

	@Bean
	public WebSocketService jettyWebSocketService() {
		return new JettyWebSocketServiceImpl();
		
	}
	
}
