package cs102groupproject.Server.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/**
 * WebSocket entry point for clients.
 */
@ServerEndpoint("/ws")
public class WebSocketServer {

    @OnOpen
    public void onOpen(Session session) {
        ClientConnection connection = new ClientConnection(session);
        session.getUserProperties().put("connection", connection);
        System.out.println("Client connected: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("📩 RAW MESSAGE FROM CLIENT: " + message);
        ClientConnection connection =
                (ClientConnection) session
                        .getUserProperties()
                        .get("connection");

        connection.onMessage(message);
    }

    @OnClose
    public void onClose(Session session) {
        ClientConnection connection =
                (ClientConnection) session
                        .getUserProperties()
                        .get("connection");

        connection.onClose();
        System.out.println("Client disconnected: " + session.getId());
    }
}
