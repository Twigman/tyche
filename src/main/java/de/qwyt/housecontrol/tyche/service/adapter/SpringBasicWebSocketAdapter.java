package de.qwyt.housecontrol.tyche.service.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import de.qwyt.housecontrol.tyche.event.RaspbeeWebSocketEvent;

public class SpringBasicWebSocketAdapter extends TextWebSocketHandler {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOG.debug("WebSocket connected with session ID: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        LOG.debug("Received message: {}", message.getPayload());
        eventPublisher.publishEvent(new RaspbeeWebSocketEvent(this, message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        LOG.debug("WebSocket connection closed: {}", status);
    }
}
