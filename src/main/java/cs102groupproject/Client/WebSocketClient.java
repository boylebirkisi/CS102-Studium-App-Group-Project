package cs102groupproject.Client;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs102groupproject.App;
import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.User;
import cs102groupproject.SharedObjects.VerificationCode;
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

    // A map to hold listeners for specific ActionTypes
    private static final Map<ActionType, List<MessageListener>> listeners = new HashMap<>();

    // Interface for the callback
    public interface MessageListener {
        void handle(Object payload);
    }

    public static void addListener(ActionType type, MessageListener listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

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
        ActionType action = msg.getAction();
        // Find and notify all registered listeners for this action
        if (listeners.containsKey(action)) {
            for (MessageListener listener : listeners.get(action)) {
                // Use Platform.runLater because UI updates must happen on the FX thread
                Platform.runLater(() -> listener.handle(msg.getPayload()));
            }
        }
    }

    public static boolean isConnected() {
        return session != null && session.isOpen();
    }
}
