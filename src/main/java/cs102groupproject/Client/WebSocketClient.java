package cs102groupproject.Client;

import java.net.URI;

import cs102groupproject.App;
import cs102groupproject.SharedObjects.ProtocolMessage;
import jakarta.websocket.Session;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.WebSocketContainer;
import javafx.application.Platform;

@ClientEndpoint
public class WebSocketClient {
     private static Session session;

    public static void connect() throws Exception {
        WebSocketContainer container =
                ContainerProvider.getWebSocketContainer();

        container.connectToServer(
                WebSocketClient.class,
                URI.create("ws://localhost:8080/ws")
        );
    }

    @OnOpen
    public void onOpen(Session s) {
        session = s;
        System.out.println("Connected to server");
    }

    public static void send(ProtocolMessage msg) {
        if (session == null) {
            System.out.println("❌ WebSocket session is NULL, message not sent");
            return;
        }

        System.out.println("➡️ Sending message: " + msg.getAction());
        session.getAsyncRemote().sendText(msg.toJson());
    }

    @OnMessage
    public void onMessage(String json) {
        System.out.println("⬅️ Message from server: " + json);

        ProtocolMessage msg = ProtocolMessage.fromJson(json);

        System.out.println("➡️ Action parsed: " + msg.getAction());

        switch (msg.getAction()) {

            case LOGIN_SUCCESS: {
                User user = (User) msg.getPayload();
                ClientSession.login(user);

                Platform.runLater(() -> {
                    try {
                        App.setRoot("secondary");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                break;
            }

            case ERROR: {
                System.out.println("Login failed: " + msg.getPayload());
                break;
            }
        }
    }


    public static boolean isConnected() {
        return session != null && session.isOpen();
    }


}
