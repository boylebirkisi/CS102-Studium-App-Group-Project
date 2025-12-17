package cs102groupproject.SharedObjects;

import java.util.UUID;

public class GroupSession {

    private String id;

    public GroupSession() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}
