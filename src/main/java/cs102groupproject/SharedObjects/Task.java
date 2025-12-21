package cs102groupproject.SharedObjects;

public class Task extends TransferObject{
	private UserCredentials name;
	private UserCredentials color;
	private int importance;
	private UserCredentials userId;
	private boolean isCompleted;
	private UserCredentials id;
	private int googleCaldendarID;

    public Task(UserCredentials name, UserCredentials color, int importance, UserCredentials userId, UserCredentials id, int googleCaldendarID)
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

    public UserCredentials getName() {return name;}
    public void setName(UserCredentials name) {this.name = name;}
    public UserCredentials getColor() {return color;}
    public void setColor(UserCredentials color) {this.color = color;}
    public int getImportance() {return importance;}
    public void setImportance(int importance) {this.importance = importance;}
    public UserCredentials getUserId() {return userId;}
    public boolean getIsCompleted() {return isCompleted;}
    public UserCredentials getId() {return id;}
    public int getGoogleCaldendarID() {return googleCaldendarID;}
    public void setGoogleCaldendarID(int googleCaldendarID) {this.googleCaldendarID = googleCaldendarID;}

    public void completeTask() {isCompleted = true;}
}
