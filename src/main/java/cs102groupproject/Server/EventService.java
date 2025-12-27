package cs102groupproject.Server;

import cs102groupproject.SharedObjects.AppEvent;
/**
 * Handles server-side operations related to application events and
 * manages interactions with the database.
 * Author: Begüm Göktaş
 * Date: 27/12/2025
 */
public class EventService {
    private final DBManager db;

    public EventService(DBManager db) {
        this.db = db;
    }

    /**
     * saves the given AppEvent to the data base
     * @param AppEvent
     */
    public AppEvent createEvent(AppEvent event) {
        int eventId = db.addEvent(event.getName(), event.getColor(), event.getStart(), event.getFinish(), event.getUserId(), event.getImportance(), event.getGoogleCaldendarID()); 
        if (eventId == -1) {
            throw new RuntimeException("Event insertion failed");
        }
        event.setId(eventId); // id came from db
        return event;
    }
}
