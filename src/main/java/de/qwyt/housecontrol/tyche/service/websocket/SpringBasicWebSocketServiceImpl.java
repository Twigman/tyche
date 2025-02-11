package de.qwyt.housecontrol.tyche.service.websocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import de.qwyt.housecontrol.tyche.service.websocket.adapter.SpringBasicWebSocketAdapter;

@Service
public class SpringBasicWebSocketServiceImpl implements WebSocketService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final WebSocketClient webSocketClient;
	
	private final SpringBasicWebSocketAdapter webSocketAdapter;
	
	private WebSocketSession session;
	
	@Autowired
	public SpringBasicWebSocketServiceImpl(WebSocketClient webSocketClient, SpringBasicWebSocketAdapter webSocketAdapter) {
		this.webSocketClient = webSocketClient;
        this.webSocketAdapter = webSocketAdapter;
	}
	
	@Override
	public void connect(String url) {
		LOG.info("Connecting to WebSocket at {}", url);

		try {
            // Verbinde den WebSocketClient mit dem WebSocketHandler
            CompletableFuture<WebSocketSession> futureSession = webSocketClient.execute(webSocketAdapter, url);

            // Sobald die Verbindung hergestellt ist, speichere die Session
            futureSession.thenAccept(session -> {
                this.session = session;
                LOG.info("WebSocket connection established with session ID: {}", session.getId());
            }).exceptionally(throwable -> {
                LOG.error("Error establishing WebSocket connection: {}", throwable.getMessage(), throwable);
                return null;
            });

        } catch (Exception e) {
        	LOG.error("Failed to connect to WebSocket at {}: {}", url, e.getMessage(), e);
        }
            
	}
	

	@Override
	public void close() {
		if (this.session != null && this.session.isOpen()) {
            try {
                this.session.close();
                LOG.info("WebSocket session disconnected");
            } catch (IOException e) {
                LOG.error("Error closing WebSocket session: {}", e.getMessage(), e);
            }
        }
	}

}
