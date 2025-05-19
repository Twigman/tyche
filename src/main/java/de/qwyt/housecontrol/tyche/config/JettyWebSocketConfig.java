package de.qwyt.housecontrol.tyche.config;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.qwyt.housecontrol.tyche.websocket.adapter.JettyWebSocketAdapter;

@Configuration
public class JettyWebSocketConfig {
	
	@Bean
	public WebSocketClient webSocketClient() {
		return new WebSocketClient(new HttpClient());
	}
	
	@Bean
	public JettyWebSocketAdapter jettyWebSocketHandler() {
		return new JettyWebSocketAdapter();
	}
}
