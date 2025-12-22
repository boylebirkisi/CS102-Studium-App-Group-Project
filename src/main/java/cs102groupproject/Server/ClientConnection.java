package cs102groupproject.Server;

import jakarta.websocket.Session;

import java.util.Map;

import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.GroupSession;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.User;
import cs102groupproject.Server.AuthService;
import cs102groupproject.Server.SessionService;

public class ClientConnection {

    /** WebSocket session associated with the client */
    private final Session socketSession;


    /** Services handling business logic */
    private static final AuthService authService = new AuthService();
    private static final SessionService sessionService = new SessionService();

    /** Logged-in user ID (null if not authenticated) */
    private String userId;

    public ClientConnection(Session session) {
        this.socketSession = session;
        userId = null;
    }

    public void onMessage(String json) {

        ProtocolMessage message = ProtocolMessage.fromJson(json);

        if (message.getAction() == null) {
            sendError("Action is null");
            return;
        }

        switch (message.getAction()) {

            //*********test case************* 
            case DEV_LOGIN: {
                Map<?, ?> payload = (Map<?, ?>) message.getPayload();
                String username = (String) payload.get("username");

                // Fake auth (TEST AMAÇLI)
                this.userId = "dev-" + username;

                System.out.println("✅ DEV_LOGIN success, userId = " + this.userId);

                send(new ProtocolMessage(
                        ActionType.LOGIN_SUCCESS,
                        this.userId
                ));
                break;
            }
            case START_GROUP_SESSION: {
                // GroupSession session =
                //         sessionService.createGroupSession(this);

                // send(new ProtocolMessage(
                //         ActionType.GROUP_SESSION_CREATED,
                //         Map.of("sessionId", session.getId())
                // ));

                break;
            }

            case JOIN_GROUP_SESSION: {
                // Map<?, ?> payload = (Map<?, ?>) message.getPayload();
                // String id = payload.get("sessionId").toString();

                // sessionService.joinGroupSession(id, this);

                // send(new ProtocolMessage(
                //         ActionType.JOINED_GROUP_SESSION,
                //         Map.of("sessionId", id)
                // ));

                break;
            }

            case LOGIN_WITH_GOOGLE: {
                Map<?, ?> payload = (Map<?, ?>) message.getPayload();
                String token = payload.get("accessToken").toString();

                User userId = AuthService.registerWithGoogle(token);

                System.out.println("✅ LOGIN_WITH_GOOGLE success, userId = " + userId.getId());

                send(new ProtocolMessage(
                        ActionType.LOGIN_SUCCESS,
                        userId
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
