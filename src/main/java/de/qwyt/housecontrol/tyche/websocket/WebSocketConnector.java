package de.qwyt.housecontrol.tyche.websocket;

public interface WebSocketConnector {
	void connect(String url);
    //void sendMessage(String message);
    void close();
}
