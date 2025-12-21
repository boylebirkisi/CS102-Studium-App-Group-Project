package cs102groupproject.Server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import cs102groupproject.SharedObjects.GroupSession;
import cs102groupproject.Server.ClientConnection;
//not completed just trial 
public class SessionService {

    private static final Map<String, GroupSession> sessions = new ConcurrentHashMap<>();

    public GroupSession createGroupSession(ClientConnection owner) {
        GroupSession session = new GroupSession();
        sessions.put(session.getId(), session);
        session.addParticipant(owner);
        return session;
    }

    public GroupSession joinGroupSession(String id, ClientConnection client) {
        GroupSession session = sessions.get(id);
        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }
        session.addParticipant(client);
        return session;
    }
}
