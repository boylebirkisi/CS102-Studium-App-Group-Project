package cs102groupproject.SharedObjects;

import java.time.LocalDate;

public class AppEvent extends TransferObject {
    private String name;
    private String color;
    private LocalDate start;
    private LocalDate finish;
    private String userId;
    private int importance;
    private int id;
    private int googleCaldendarID;

    public AppEvent(String name, String color, LocalDate start, LocalDate finish, String userId, int importance, int id, int googleCaldendarID)
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

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}
    public LocalDate getStart() {return start;}
    public void setStart(LocalDate start) {this.start = start;}
    public LocalDate getFinish() {return finish;}
    public void setFinish(LocalDate finish) {this.finish = finish;}
    public String getUserId() {return userId;}
    public int getImportance() {return importance;}
    public int getId() {return id;}
    public int getGoogleCaldendarID() {return googleCaldendarID;}

    public boolean getIsExpired() 
    {
        if (LocalDate.now().isAfter(finish))
        {
            return true;
        }
        return false;
    }
}