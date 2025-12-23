package cs102groupproject.Server;

import jakarta.websocket.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import cs102groupproject.SharedObjects.*;
import cs102groupproject.Server.AuthService;
import cs102groupproject.Server.SessionService;

public class ClientConnection {

    /** WebSocket session associated with the client */
    private final Session socketSession;
    private final SessionManager sessionManager;


    /** Services handling business logic */
    private static final AuthService authService = new AuthService();
    private static final SessionService sessionService = new SessionService(new DBManager());
    private static final EventService eventService = new EventService(new DBManager());
    private static final HabitService habitService = new HabitService(new DBManager());

    /** Logged-in user ID (null if not authenticated) */
    private int userId;
    private User loggedInUser;

    public ClientConnection(Session session, SessionManager sessionManager) {
        this.socketSession = session;
        this.sessionManager = sessionManager;
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
                UserCredentials credentials = message.getPayloadAs(UserCredentials.class);

                User user = AuthService.login(credentials);

                send(new ProtocolMessage(
                        ActionType.LOGIN_SUCCESS,
                        user
                ));
                break;
            }
                
            case SEND_PRIVATE_MESSAGE: {
                ChatMessage chatMsg = message.getPayloadAs(ChatMessage.class);
                ClientConnection receiverConn = sessionManager.getConnection(chatMsg.getReceiverID());
                if (receiverConn != null) {
                    receiverConn.send(new ProtocolMessage(ActionType.RECEIVE_PRIVATE_MESSAGE, chatMsg));
                } 
                else {
                    System.out.println("The receiver is offline: " + chatMsg.getReceiverID());
                }
                break;
            }

            case CREATE_HABIT:{
                Habit habit = message.getPayloadAs(Habit.class);
                Habit saved = habitService.updateHabit(habit); 
                
                send(new ProtocolMessage(ActionType.HABIT_CREATED, saved));
                break;
            }
                
            case UPDATE_HABIT: {
                Habit habit = message.getPayloadAs(Habit.class);
                habitService.updateHabit(habit); 
                
                send(new ProtocolMessage(ActionType.HABIT_UPDATED, habit));
                break;
            }

            case CREATE_EVENT: {
                if (loggedInUser == null) {
                    sendError("Not authenticated");
                    return;
                }

                AppEvent event = message.getPayloadAs(AppEvent.class);
                
                try {
                    AppEvent savedEvent = eventService.createEvent(event);
                    // feedback for test purposes
                    send(new ProtocolMessage(ActionType.EVENT_CREATED, savedEvent));
                } 
                catch (Exception e) {
                    sendError("Could not create event: " + e.getMessage());
                }
                break;
            }

            //create group session pop-up handling
            case CREATE_GROUP_SESSION: {
                if (loggedInUser == null) {
                    sendError("Not authenticated");
                    return;
                }

                Map<?, ?> payload = (Map<?, ?>) message.getPayload();

                String sessionName = payload.get("sessionName").toString();
                int sessionNo = Integer.parseInt(payload.get("sessionNo").toString());
                int sessionLength = Integer.parseInt(payload.get("sessionLength").toString());
                int breakLength = Integer.parseInt(payload.get("breakLength").toString());
                boolean isPublic = Boolean.parseBoolean(payload.get("isPublic").toString());

                LocalDateTime startDate =
                    LocalDateTime.parse(payload.get("startDate").toString());

                User owner = this.loggedInUser;

                GroupSession session = new GroupSession(
                    owner,
                    new ArrayList<>(),
                    sessionName,
                    "GroupSession",
                    sessionNo,
                    sessionLength,
                    breakLength,
                    startDate,
                    isPublic
                );

                GroupSession saved = sessionService.createGroupSession(session);

                send(new ProtocolMessage(
                    ActionType.GROUP_SESSION_CREATED,
                    saved
                ));

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
                String token = message.getPayloadAs(String.class);

                User user = AuthService.loginWithGoogle(token);

                if (user == null) {
                    sendError("LOGIN_WITH_GOOGLE failed");
                    return;
                }
                this.loggedInUser = user;
                this.userId = user.getId();
                sessionManager.login(user.getId(), this);
                
                System.out.println("✅ LOGIN_WITH_GOOGLE success, userId = " + user.getId());

                send(new ProtocolMessage(
                        ActionType.LOGIN_SUCCESS,
                        user
                ));
                break;
            }

            case REGISTER: {
                UserCredentials credentials = message.getPayloadAs(UserCredentials.class);

                User user = AuthService.register(credentials);

                if (user != null) {
                    this.loggedInUser = user;
                    this.userId = user.getId();
                    send(new ProtocolMessage(
                        ActionType.REGISTER_SUCCESS,
                        user
                    ));
                }
                break;
            }

            case REGISTER_WITH_GOOGLE: {
                UserCredentials credentials = message.getPayloadAs(UserCredentials.class);

                User user = AuthService.registerWithGoogle(credentials);
                if (user != null) {
                    this.loggedInUser = user;
                    this.userId = user.getId();
                    send(new ProtocolMessage(
                        ActionType.REGISTER_SUCCESS,
                        user
                    ));
                }
                break;
            }

            case SEND_VERIFICATION_CODE: {
                String email = message.getPayloadAs(String.class);

                VerificationCode verificationCode = AuthService.sendVerificationCode(email);
                if (verificationCode == null) {
                    sendError("Failed to send verification code");
                    return;
                }

                // We do not need to inform client for this
                System.out.println("✅ Verification code sent to " + email);
                break;
            }

            case VERIFY_CODE: {
                UserCredentials credentials = message.getPayloadAs(UserCredentials.class);
                String email = credentials.getUsername();
                String code = credentials.getPassword();

                boolean success = AuthService.verifyCode(email, code);

                if (success) {
                    send(new ProtocolMessage(
                        ActionType.CODE_SUCCESS, 
                        null));
                } else {
                    send(new ProtocolMessage(
                        ActionType.CODE_FAILURE, 
                        null));
                }
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
