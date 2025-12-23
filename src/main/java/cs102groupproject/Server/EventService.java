package cs102groupproject.Server;

import cs102groupproject.SharedObjects.AppEvent;

public class EventService {
    private final DBManager db;

    public EventService(DBManager db) {
        this.db = db;
    }

    public AppEvent createEvent(AppEvent event) {
        int eventId = db.addEvent(event.getName(), event.getColor(), event.getStart(), event.getFinish(), event.getUserId(), event.getImportance(), event.getGoogleCaldendarID()); 
        if (eventId == -1) {
            throw new RuntimeException("Event insertion failed");
        }
        event.setId(eventId); // id came from db
        return event;
    }
}
