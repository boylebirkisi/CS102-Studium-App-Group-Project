package cs102groupproject.Server;
import cs102groupproject.SharedObjects.*;
/**
 * Responsible for sending notifications to clients. Used to deliver server-side updates asynchronously.
 */
public class NotificationService {
    public static void sendFriendRequest(int userID, int referenceID, String notificationText) {
        Notification notif = new Notification(userID, referenceID, notificationText, "New Friend Request", null, Notification.Type.FRIEND_REQUEST);
        OptionalNotification optNotif = new OptionalNotification(
            userID,
            referenceID,
            notificationText,
            "New Friend Request",
            null,
            "Accept",
            "Decline",
            Notification.Type.FRIEND_REQUEST
        );
    }

    public static void sendPDFRequest(int userID, int referenceID, String notification) {
        Notification notif = new Notification(userID, referenceID, "sent you a PDF access request.", "New PDF Request", null, Notification.Type.PDF_REQUEST);
        OptionalNotification optNotif = new OptionalNotification(
            userID,
            referenceID,
            notification,
            "New PDF Request",
            null,
            "Accept",
            "Decline",
            Notification.Type.PDF_REQUEST
        );
    }

    public static Notification sendInfoNotification(int userID, String notificationText, String heading) {
        Notification notif = new Notification(userID, -1, notificationText, heading, null, Notification.Type.INFORMATIVE);
        return notif;
    }
}
