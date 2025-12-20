package cs102groupproject.client;

import jakarta.websocket.*;
import java.net.URI;
import java.util.Map;

import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.ProtocolMessage;

@ClientEndpoint
public class AttendantClientTester {

    @OnOpen
    public void onOpen(Session session) {

        // ID oluşana kadar bekle (TEST AMAÇLI)
        while (TestClient.getGroupSessionId() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }

        ProtocolMessage msg =
                new ProtocolMessage(
                        ActionType.JOIN_GROUP_SESSION,
                        Map.of("sessionId", TestClient.getGroupSessionId())
                );

        session.getAsyncRemote().sendText(msg.toJson());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Server response: " + message);
    }

    @OnClose
    public void onClose() {
        System.out.println("Connection closed");
    }

    public static void main(String[] args) throws Exception {
        WebSocketContainer container =
                ContainerProvider.getWebSocketContainer();

        container.connectToServer(
                AttendantClientTester.class,
                URI.create("ws://localhost:8080/ws")
        );

        Thread.sleep(20_000);
    }
}
