package cs102groupproject.SharedObjects;

import javafx.scene.paint.Color;

public class Task extends TransferObject{
	private String name;
	private Color color;
	private int importance;
	private int userId;
	private boolean isCompleted;
	private int id;
	private String googleCaldendarID;

    public Task(String name, Color color, int importance, int userId, int id, String googleCaldendarID)
    {
        this.name = name;
        this.color = color;
        this.importance = importance;
        this.userId = userId;
        isCompleted = false;
        this.id = id;
        this.googleCaldendarID = googleCaldendarID;
        this.objectType = "Task";
    }

    public Task(String name, Color color, int importance, int userId, String googleCaldendarID)
    {
        this.name = name;
        this.color = color;
        this.importance = importance;
        this.userId = userId;
        isCompleted = false;
        id = -1; //indicates that the task has not been assigned an ID yet
        this.googleCaldendarID = googleCaldendarID;
        this.objectType = "Task";
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Color getColor() {return color;}
    public void setColor(Color color) {this.color = color;}
    public int getImportance() {return importance;}
    public void setImportance(int importance) {this.importance = importance;}
    public int getUserId() {return userId;}
    public boolean getIsCompleted() {return isCompleted;}
    public int getId() {return id;}
    public String getGoogleCaldendarID() {return googleCaldendarID;}
    public void setGoogleCaldendarID(String googleCaldendarID) {this.googleCaldendarID = googleCaldendarID;}

    public void completeTask() {isCompleted = true;}
}
