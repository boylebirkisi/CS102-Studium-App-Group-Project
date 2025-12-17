package cs102groupproject.server.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cs102groupproject.SharedObjects.GroupSession;
import cs102groupproject.server.model.entity.SessionEntity;
import cs102groupproject.server.websocket.ClientConnection;


public class SessionService {

    private final Map<String, GroupSession> sessions = new ConcurrentHashMap<>();

    public GroupSession createGroupSession() {
        GroupSession s = new GroupSession();
        sessions.put(s.getId(), s);
        return s;
    }
}




// package cs102groupproject.server.service;

// import java.time.LocalDateTime;
// import java.util.UUID;

// import cs102groupproject.server.model.entity.SessionEntity;
// import cs102groupproject.server.protocol.ProtocolMessage;
// import cs102groupproject.server.protocol.requests.StartSessionRequest;
// import cs102groupproject.server.repository.SessionRepository;
// import cs102groupproject.server.repository.UserRepository;
// import cs102groupproject.server.util.JsonUtil;
// import cs102groupproject.server.websocket.ClientConnection;


//Map<String, Session>
// /**
//  * Controls session lifecycle and reward calculation. Applies business rules and updates session-related data.
//  */
// public class SessionService {
//     // private SessionRepository repo;
//     // private UserRepository userRepo;

//     // public void startSession(ProtocolMessage msg, String userId) {
//     //     StartSessionRequest req =
//     //         JsonUtil.fromPayload(msg, StartSessionRequest.class);

//     //     SessionEntity session = new SessionEntity(...);
//     //     repo.save(session);
//     // }

//     // public void endSession(String sessionId) {
//     //     // currency calculation here
//     // }   

//     private final SessionRepository sessionRepository = new SessionRepository();

//     public void startSession(ProtocolMessage msg, String userId, ClientConnection client) {
//         if (userId == null) {
//             client.sendResponse(new ErrorResponse("Not authenticated"));
//             return;
//         }

//         StartSessionRequest req = JsonUtil.fromJson(msg, StartSessionRequest.class);

//         SessionEntity session = new SessionEntity(
//             UUID.randomUUID().toString(),
//             userId,
//             SessionType.SOLO, //// LOOK AGAIN
//             LocalDateTime.now(),
//             null
//         );

//         sessionRepository.save(session);

//         client.sendResponse(
//             new SessionStartedResponse(session.getId())
//         );}

// }
