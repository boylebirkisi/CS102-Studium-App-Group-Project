package cs102groupproject.Server.model.entity;

/**
 * Represents a session stored in the database. Tracks session state and is managed by SessionService.
 */
public class SessionEntity {
    private String id;
    private String userId;
    private int duration;
    private String sessionType;
    private boolean completed;
}
