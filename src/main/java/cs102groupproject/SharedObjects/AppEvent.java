package cs102groupproject.SharedObjects;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppEvent extends TransferObject {
    private String name;
    private String color;
    private LocalDateTime start;
    private LocalDateTime finish;
    private int userId;
    private int importance;
    private int id;
    private String googleCaldendarID;

    public AppEvent(String name, String color, LocalDateTime start, LocalDateTime finish, int userId, int importance, int id, String googleCaldendarID)
    {
        this.name = name;
        this.color = color;
        this.start = start;
        this.finish = finish;
        this.userId = userId;
        this.importance = importance;
        this.id = id;
        this.googleCaldendarID = googleCaldendarID;
        objectType = "Event";
    }

    public AppEvent(String name, String color, LocalDateTime start, LocalDateTime finish, int userId, int importance, String googleCaldendarID)
    {
        this.name = name;
        this.color = color;
        this.start = start;
        this.finish = finish;
        this.userId = userId;
        this.importance = importance;
        id = -1; //indicates that the event has not been assigned an ID yet
        this.googleCaldendarID = googleCaldendarID;
        objectType = "Event";
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}
    public LocalDateTime getStart() {return start;}
    public void setStart(LocalDateTime start) {this.start = start;}
    public LocalDateTime getFinish() {return finish;}
    public void setFinish(LocalDateTime finish) {this.finish = finish;}
    public int getUserId() {return userId;}
    public int getImportance() {return importance;}
    public int getId() {return id;}
    public String getGoogleCaldendarID() {return googleCaldendarID;}

    public boolean getIsExpired() 
    {
        if (LocalDateTime.now().isAfter(finish))
        {
            return true;
        }
        return false;
    }
}