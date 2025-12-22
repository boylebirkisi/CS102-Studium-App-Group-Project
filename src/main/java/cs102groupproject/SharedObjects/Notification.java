package cs102groupproject.SharedObjects;

public class Notification extends TransferObject{
    private String notificationText;
    private String heading;
    private int id;
<<<<<<< HEAD
    private int userID;
    // Either a UserID, SessionID, PDFID etc.
    private int referenceID;
=======
>>>>>>> main
    private String png;
    private boolean isRemoved;
    private enum Status {Pending, Accepted, Declined};
    private Status status;
    enum Type {FriendRequest, PDFRequest, SessionInvite, Informative};
    private Type type;

    public Notification(String notificationText, String heading, int id, String png, Type type)
    {
        this.notificationText = notificationText;
        this.heading = heading;
        this.id = id;
        this.png = png;
        this.type = type;
        isRemoved = false;
        objectType = "Notification";
        setStatus(Status.Pending);
    }

    public Notification(String notificationText, String heading, String png, Type type)
    {
        this.notificationText = notificationText;
        this.heading = heading;
        id = -1; //indicates that the notification has not been assigned an ID yet
        this.png = png;
        this.type = type;
        isRemoved = false;
        objectType = "Notification";
        setStatus(Status.Pending);
    }

    public String getNotificationText() {return notificationText;}
    public String getHeading() {return heading;}
    public int getId() {return id;}
    public String getPng() {return png;}
    public boolean getIsRemoved() {return isRemoved;}
    public void removeNotification() {isRemoved = true;}
    public Status getStatus() {return status;}
    public void setStatus(Status status) {this.status = status;}
    public Type getType() {return type;}
    public void setType(Type type) {this.type = type;}
}
