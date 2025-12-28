package cs102groupproject.SharedObjects;
/**
 * Class representing a notification sent to a user.
 * @author Ali Mersin
 */
public class Notification extends TransferObject{
    private String notificationText;
    private String heading;
    private int id;
    private int userID;
    // Either a UserID, SessionID, PDFID etc.
    private int referenceID;
    private String png;
    private boolean isRemoved;
    public enum Status {PENDING, ACCEPTED, DECLINED};
    private Status status;
    public enum Type {FRIEND_REQUEST, PDF_REQUEST, SESSION_INVITE, SESSION_ACCESS_REQUEST, INFORMATIVE};
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
        setStatus(Status.PENDING);
    }

    public Notification(int userID, int referenceID, String notificationText, String heading, String png, Type type)
    {
        this.notificationText = notificationText;
        this.heading = heading;
        this.userID = userID;
        this.referenceID = referenceID;
        this.png = png;
        this.type = type;
        isRemoved = false;
        objectType = "Notification";
        setStatus(Status.PENDING);
    }

    public String getNotificationText() {return notificationText;}
    public String getHeading() {return heading;}
    public int getId() {return id;}
    public int getUserID() {return userID;}
    public int getReferenceID() {return referenceID;}
    public String getPng() {return png;}
    public boolean getIsRemoved() {return isRemoved;}
    public void removeNotification() {isRemoved = true;}
    public Status getStatus() {return status;}
    public void setStatus(Status status) {this.status = status;}
    public Type getType() {return type;}
    public void setType(Type type) {this.type = type;}
}
