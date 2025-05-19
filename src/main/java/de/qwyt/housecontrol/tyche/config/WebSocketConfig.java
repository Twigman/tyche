package de.qwyt.housecontrol.tyche.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import de.qwyt.housecontrol.tyche.websocket.JettyWebSocketConnectorImpl;
import de.qwyt.housecontrol.tyche.websocket.SpringBasicWebSocketConnectorImpl;
import de.qwyt.housecontrol.tyche.websocket.WebSocketConnector;
import de.qwyt.housecontrol.tyche.websocket.adapter.SpringBasicWebSocketAdapter;

import org.springframework.web.socket.client.WebSocketClient;

@Configuration
public class WebSocketConfig {
	
	@Bean
	public SpringBasicWebSocketAdapter springBasicWebSocketAdapter() {
		return new SpringBasicWebSocketAdapter();
	}
	
	@Bean
	public WebSocketClient springWebSocketClient() {
		return new StandardWebSocketClient();
	}

	@Bean
	public WebSocketConnector springBasicWebSocketConnector(WebSocketClient webSocketClient, SpringBasicWebSocketAdapter sessionAdapter) {
		return new SpringBasicWebSocketConnectorImpl(webSocketClient, sessionAdapter);
	}

	@Bean
	public WebSocketConnector jettyWebSocketConnector() {
		return new JettyWebSocketConnectorImpl();
		
	}
}
