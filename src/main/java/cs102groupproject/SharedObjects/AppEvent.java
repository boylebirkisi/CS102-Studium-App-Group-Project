package cs102groupproject.SharedObjects;

import java.time.LocalDate;

public class AppEvent extends TransferObject {
    private String name;
    private String color;
    private LocalDate start;
    private LocalDate finish;
    private int userId;
    private int importance;
    private int id;
    private String googleCaldendarID;

    public AppEvent(String name, String color, LocalDate start, LocalDate finish, int userId, int importance, int id, String googleCaldendarID)
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

    public AppEvent(String name, String color, LocalDate start, LocalDate finish, int userId, int importance, String googleCaldendarID)
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
    public LocalDate getStart() {return start;}
    public void setStart(LocalDate start) {this.start = start;}
    public LocalDate getFinish() {return finish;}
    public void setFinish(LocalDate finish) {this.finish = finish;}
    public int getUserId() {return userId;}
    public int getImportance() {return importance;}
    public int getId() {return id;}
    public String getGoogleCaldendarID() {return googleCaldendarID;}

    public boolean getIsExpired() 
    {
        if (LocalDate.now().isAfter(finish))
        {
            return true;
        }
        return false;
    }
}