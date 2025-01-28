package de.qwyt.housecontrol.tyche.service;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.qwyt.housecontrol.tyche.service.adapter.JettyWebSocketAdapter;

@Service
public class JettyWebSocketServiceImpl implements WebSocketService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WebSocketClient webSocketClient;
	
	@Autowired
	private JettyWebSocketAdapter webSocketAdapter;
	
	private Session session;


	@Override
	public void connect(String url) {
		if (webSocketClient.isStarted()) {
			LOG.info("The WebSocket is already started");
		} else {
			LOG.info("Trying to start WebSocket via HTTP/1.1");
			
			try {
				this.webSocketClient.start();
				LOG.info("Connecting to WebSocket via Jetty at {}", url);
				URI uri = new URI(url);
				
				this.session = webSocketClient.connect(webSocketAdapter, uri).get();
				
				if (this.session.isOpen()) {
					LOG.info("WebSocket is connected");
				}
				
			} catch (Exception e) {
				LOG.error("Failed to connect to WebSocket via Jetty: {}", e.getMessage());
			}
		}
	}

	@Override
	public void close() {
		try {
			if (this.session != null) {
				this.session.close();
			}
			this.webSocketClient.stop();
			LOG.info("Jetty WebSocket client stopped");
		} catch (Exception e) {
			LOG.error("Error stopping Jetty WebSocket client: {}", e.getMessage());
		}
	}
}
