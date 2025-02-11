package de.qwyt.housecontrol.tyche.service.websocket;

public interface WebSocketService {
	void connect(String url);
    //void sendMessage(String message);
    void close();
}
