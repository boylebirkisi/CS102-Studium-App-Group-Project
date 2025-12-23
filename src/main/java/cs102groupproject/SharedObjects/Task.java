package cs102groupproject.SharedObjects;

import java.time.LocalDate;

public class Task extends TransferObject{
	private String name;
	private String color;
	private int importance;
	private int userId;
	private boolean isCompleted;
    private LocalDate dueDate;
	private int id;
	private String googleCaldendarID;

    public Task(String name, String color, int importance, int userId, int id, LocalDate dueDate, String googleCaldendarID, boolean isCompleted)
    {
        this.name = name;
        this.color = color;
        this.importance = importance;
        this.userId = userId;
        this.isCompleted = isCompleted;
        this.id = id;
        this.dueDate = dueDate;
        this.googleCaldendarID = googleCaldendarID;
        this.objectType = "Task";
    }

    public Task(String name, String color, int importance, int userId, LocalDate dueDate, String googleCaldendarID)
    {
        this.name = name;
        this.color = color;
        this.importance = importance;
        this.userId = userId;
        this.dueDate = dueDate;
        isCompleted = false;
        id = -1; //indicates that the task has not been assigned an ID yet
        this.googleCaldendarID = googleCaldendarID;
        this.objectType = "Task";
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}
    public int getImportance() {return importance;}
    public void setImportance(int importance) {this.importance = importance;}
    public int getUserId() {return userId;}
    public boolean getIsCompleted() {return isCompleted;}
    public int getId() {return id;}
    public LocalDate getDueDate() {return dueDate;}
    public void setDueDate(LocalDate dueDate) {this.dueDate = dueDate;}
    public String getGoogleCaldendarID() {return googleCaldendarID;}
    public void setGoogleCaldendarID(String googleCaldendarID) {this.googleCaldendarID = googleCaldendarID;}

    public void completeTask() {isCompleted = true;}
    public void uncompleteTask() {isCompleted = false;}
}   