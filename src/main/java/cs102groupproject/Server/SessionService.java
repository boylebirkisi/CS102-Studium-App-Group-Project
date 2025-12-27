package cs102groupproject.Server;

import java.util.*;

import cs102groupproject.SharedObjects.GroupSession;
/**
 * performs part of the session creation-related operations and 
 * manages interactions with the database.
 * @author Begüm Göktaş
 * Date: 27/12/2025
 */
public class SessionService {

    private final DBManager db;

    public SessionService(DBManager db) {
        this.db = db;
    }

    /**
     * saves the given GroupSession to the database
     */
    public GroupSession createGroupSession(GroupSession groupSession) {

        int sessionId = db.addSession(groupSession);
        if (sessionId == -1) {
            throw new RuntimeException("Session insert failed");
        }

        groupSession.setId(sessionId);
        return groupSession;
    }
}
