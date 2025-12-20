package cs102groupproject.SharedObjects;

public class Task extends TransferObject{
	private String name;
	private String color;
	private int importance;
	private String userId;
	private boolean isCompleted;
	private String id;
	private int googleCaldendarID;

    public Task(String name, String color, int importance, String userId, String id, int googleCaldendarID)
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

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}
    public int getImportance() {return importance;}
    public void setImportance(int importance) {this.importance = importance;}
    public String getUserId() {return userId;}
    public boolean getIsCompleted() {return isCompleted;}
    public String getId() {return id;}
    public int getGoogleCaldendarID() {return googleCaldendarID;}
    public void setGoogleCaldendarID(int googleCaldendarID) {this.googleCaldendarID = googleCaldendarID;}

    public void completeTask() {isCompleted = true;}
}
