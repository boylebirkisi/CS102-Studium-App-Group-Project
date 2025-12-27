package cs102groupproject.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

/**
 * responsible for managing the client-side WebSocket connection.
 * Establishes and maintains a WebSocket session with the server and
 * dispatches incoming messages to registered listeners based on their action type
 * Authors: Delfin Eryılmaz / Begüm Göktaş(partially)
 */   
@ClientEndpoint
public class WebSocketClient {
    private static Session session;

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(java.time.LocalDateTime.class, (JsonSerializer<java.time.LocalDateTime>) (src, typeOfSrc, context) ->
                new JsonPrimitive(src.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .registerTypeAdapter(java.time.LocalDateTime.class, (JsonDeserializer<java.time.LocalDateTime>) (json, typeOfT, context) ->
                java.time.LocalDateTime.parse(json.getAsString(), java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        .registerTypeAdapter(java.time.LocalDate.class, (JsonSerializer<java.time.LocalDate>) (src, typeOfSrc, context) ->
                new JsonPrimitive(src.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)))
        .registerTypeAdapter(java.time.LocalDate.class, (JsonDeserializer<java.time.LocalDate>) (json, typeOfT, context) ->
                java.time.LocalDate.parse(json.getAsString(), java.time.format.DateTimeFormatter.ISO_LOCAL_DATE))
        .create();

    public static Gson getGson() {
        return gson;
    }
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
            System.out.println("WebSocket session is NULL, message not sent");
            return;
        }

        System.out.println("Sending message: " + msg.getAction());
        session.getAsyncRemote().sendText(msg.toJson());
    }

    @OnMessage
    public void onMessage(String json) {
        try {
            System.out.println("Data received from the server " + json);

            ProtocolMessage msg = getGson().fromJson(json, ProtocolMessage.class);

            if (msg == null || msg.getAction() == null) {
                System.err.println("Error! Message or action is null");
                return;
            }

            System.out.println("Processed action: " + msg.getAction());
            ActionType action = msg.getAction();

            if (listeners.containsKey(action)) {
                for (MessageListener listener : listeners.get(action)) {
                    Platform.runLater(() -> {
                        try {
                            listener.handle(msg.getPayload());
                        } catch (Exception e) {
                            System.err.println("There occured a problem during UI update " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                }
            } else {
                System.out.println("No listener is here to get action " + action);
            }
        } catch (Exception e) {
            System.err.println("critical mistake in OnMessage method " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isConnected() {
        return session != null && session.isOpen();
    }
}
