package cs102groupproject.Server;

import java.util.*;

import cs102groupproject.SharedObjects.GroupSession;
//not completed yet
public class SessionService {

    private final DBManager db;

    public SessionService(DBManager db) {
        this.db = db;
    }

    public GroupSession createGroupSession(GroupSession groupSession) {

        int sessionId = db.addSession(groupSession);
        if (sessionId == -1) {
            throw new RuntimeException("Session insert failed");
        }

        groupSession.setId(sessionId);

        // // group_sessions tablosu
        // db.addGroupSession(sessionId, groupSession.isPublic());

        // // owner participant olarak eklenmeli bence
        // db.addSessionParticipant(sessionId, groupSession.getOwner().getId());

        return groupSession;
    }
}
