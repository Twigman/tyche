package de.qwyt.housecontrol.tyche.model.event;


import org.springframework.context.ApplicationEvent;

public class RaspbeeWebSocketEvent extends ApplicationEvent {
	
	private Object source;
	
	private String message;
	//private RaspbeeMessage webSocketMessage;
	
	//private ObjectMapper objectMapper;
	
	public RaspbeeWebSocketEvent(Object source, String message) {
		super(source);
		
		this.source = source;
		this.message = message;
		/*
		objectMapper = new ObjectMapper();
		try {
			this.webSocketMessage = objectMapper.readValue(message, RaspbeeMessage.class);
		} catch (JsonMappingException e) {
			LOG.error("Couldn't parse websocket message from JSON to WebSocketMessage object.");
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			LOG.error("Couldn't parse websocket message from JSON to WebSocketMessage object.");
			e.printStackTrace();
		}*/
	}
	
	public String getMessage() {
		return this.message;
	}

}
