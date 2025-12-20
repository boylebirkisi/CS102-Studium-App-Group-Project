package cs102groupproject.client;

import java.net.URI;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;

public class TestRunner {

    public static void main(String[] args) throws Exception {
        WebSocketContainer container =
                ContainerProvider.getWebSocketContainer();

        // Owner
        container.connectToServer(
                TestClient.class,
                URI.create("ws://localhost:8080/ws")
        );

        Thread.sleep(1000);

        // Attendant
        container.connectToServer(
                AttendantClientTester.class,
                URI.create("ws://localhost:8080/ws")
        );

        Thread.sleep(20_000);
    }
}
