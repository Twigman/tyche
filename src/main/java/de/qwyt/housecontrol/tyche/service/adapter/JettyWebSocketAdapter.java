package de.qwyt.housecontrol.tyche.service.adapter;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketOpen;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import de.qwyt.housecontrol.tyche.event.RaspbeeWebSocketEvent;

@WebSocket(autoDemand = true)
public class JettyWebSocketAdapter {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        LOG.info("Connection closed - code: " + statusCode + ", reason: " + reason);
        //this.session = null;
    }
	
	@OnWebSocketOpen
    public void onOpen(Session session) {
		LOG.info("WebSocket connection opened.");
        //this.session = session;
    }
		
	@OnWebSocketMessage
    public void onMessage(String msg) {
		LOG.debug(msg);
		eventPublisher.publishEvent(new RaspbeeWebSocketEvent(this, msg));
    }
}
