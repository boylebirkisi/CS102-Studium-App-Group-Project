package cs102groupproject.SharedObjects;

import java.time.LocalDate;

public class AppEvent extends TransferObject {
    private String name;
    private String color;
    private LocalDate start;
    private LocalDate finish;
    private String userId;
    private int importance;
    private String id;
    private String googleCalendarID;
    private boolean isExpired;

    public AppEvent() {}

    // Constructor for new events which are not in database yet
    public AppEvent(String name, String color, LocalDate start, LocalDate finish, String userId, int importance, String googleCalendarID) {
        this.id = null; 
        this.name = name;
        this.color = color;
        this.start = start;
        this.finish = finish;
        this.userId = userId;
        this.importance = importance;
        this.googleCalendarID = googleCalendarID;
        this.isExpired = isExpired();
    }

    // Constructor for events already in database
    public AppEvent(String id, String name, String color, LocalDate start, LocalDate finish, String userId, int importance, String googleCalendarID) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.start = start;
        this.finish = finish;
        this.userId = userId;
        this.importance = importance;
        this.googleCalendarID = googleCalendarID;
        this.isExpired = isExpired();
    }

    public boolean isExpired() {
        return (finish != null && finish.isBefore(LocalDate.now()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getFinish() {
        return finish;
    }

    public void setFinish(LocalDate finish) {
        this.finish = finish;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogleCalendarID() {
        return googleCalendarID;
    }

    public void setGoogleCalendarID(String googleCalendarID) {
        this.googleCalendarID = googleCalendarID;
    }
}
