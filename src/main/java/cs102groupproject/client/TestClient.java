package cs102groupproject.client;

import jakarta.websocket.*;
import java.net.URI;

import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.ProtocolMessage;

@ClientEndpoint
public class TestClient {

    @OnOpen
    public void onOpen(Session session) {
        ProtocolMessage msg =
                new ProtocolMessage(ActionType.START_GROUP_SESSION, null);

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
                TestClient.class,
                URI.create("ws://localhost:8080/ws")
        );

        Thread.sleep(10_000);
    }
}
