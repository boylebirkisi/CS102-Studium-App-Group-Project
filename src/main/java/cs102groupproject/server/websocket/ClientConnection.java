package cs102groupproject.server.websocket;

import jakarta.websocket.Session;

import java.util.Map;

import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.GroupSession;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.server.service.SessionService;

public class ClientConnection {

    private final Session socketSession;
    private final SessionService sessionService = new SessionService();

    private String currentGroupSessionId;

    public ClientConnection(Session session) {
        this.socketSession = session;
    }

    public void onMessage(String json) {

        ProtocolMessage message = ProtocolMessage.fromJson(json);

        if (message.getAction() == null) {
            sendError("Action is null");
            return;
        }

        switch (message.getAction()) {

            case START_GROUP_SESSION: {
                GroupSession session =
                        sessionService.createGroupSession(this);

                send(new ProtocolMessage(
                        ActionType.GROUP_SESSION_CREATED,
                        Map.of("sessionId", session.getId())
                ));

                break;
            }

            case JOIN_GROUP_SESSION: {
                Map<?, ?> payload = (Map<?, ?>) message.getPayload();
                String id = payload.get("sessionId").toString();

                sessionService.joinGroupSession(id, this);

                send(new ProtocolMessage(
                        ActionType.JOINED_GROUP_SESSION,
                        Map.of("sessionId", id)
                ));

                break;
            }

            default: sendError("Unknown action");
        }
    }

    private void send(ProtocolMessage msg) {
        try {
            socketSession.getBasicRemote()
                         .sendText(msg.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendError(String error) {
        send(new ProtocolMessage(ActionType.ERROR, error));
    }

    public void onClose() {
        System.out.println("ClientConnection closed");
    }
}


    // /** WebSocket session associated with the client */
    // private final Session socketSession;

    // /** Logged-in user ID (null if not authenticated) */
    // private String userId;

    // /** Services handling business logic */
    // private final AuthService authService;
    // private final UserService userService;
    // private final SessionService sessionService;

    // public ClientConnection(Session socketSession) {
    //     this.socketSession = socketSession;
    //     this.authService = new AuthService();
    //     this.userService = new UserService();
    //     this.sessionService = new SessionService();
    // }

    // /**
    //  * Called when a message is received from the client.
    //  */
    // public void onMessage(String json) {
    //     ProtocolMessage message = JsonUtil.fromJson(json, ProtocolMessage.class);
    //     handleMessage(message);
    // }

    // /**
    //  * Dispatches the received protocol message to the appropriate service.
    //  */
    // private void handleMessage(ProtocolMessage message) {
    //     ActionType action = message.getAction();

    //     switch (action) {

    //         case LOGIN:
    //             authService.login(message, this);

    //         case REGISTER:
    //             authService.register(message, this);

    //         case START_SESSION:
    //             sessionService.startSession(message, userId, this);

    //         case END_SESSION:
    //             sessionService.endSession(userId, this);

    //         default:
    //             sendError("Unsupported action: " + action);
    //     }
    // }

    // /**
    //  * Sends a response object to the client as JSON.
    //  */
    // public void sendResponse(Object response) {
    //     try {
    //         String json = JsonUtil.toJson(response);
    //         socketSession.getBasicRemote().sendText(json);
    //     } catch (IOException e) {
    //         System.err.println("Failed to send message to client: " + e.getMessage());
    //     }
    // }

    // /**
    //  * Sends an error message to the client.
    //  */
    // private void sendError(String message) {
    //     sendResponse(new ProtocolMessage(ActionType.ERROR, message));
    // }

    // /**
    //  * Called when the client disconnects.
    //  */
    // public void onClose() {
    //     if (userId != null) {
    //         sessionService.handleDisconnect(userId);
    //     }
    //     System.out.println("Client disconnected: " + socketSession.getId());
    // }

    // // ---------- Getters & Setters -------------

    // public String getUserId() {
    //     return userId;
    // }

    // public void setUserId(String userId) {
    //     this.userId = userId;
    // }

    // public Session getSocketSession() {
    //     return socketSession;
    // }
