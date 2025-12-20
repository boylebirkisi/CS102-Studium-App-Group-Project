package cs102groupproject.SharedObjects;

public class Notification extends TransferObject{
    private String notificationText;
    private String heading;
    private String id;
    private String png;
    private boolean isRemoved;

    public Notification(String notificationText, String heading, String id, String png)
    {
        this.notificationText = notificationText;
        this.heading = heading;
        this.id = id;
        this.png = png;
        isRemoved = false;
        objectType = "Notification";
    }

    public String getNotificationText() {return notificationText;}
    public String getHeading() {return heading;}
    public String getId() {return id;}
    public String getPng() {return png;}
    public boolean getIsRemoved() {return isRemoved;}
    public void removeNotification() {isRemoved = true;}
}
