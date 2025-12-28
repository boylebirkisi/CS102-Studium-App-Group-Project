package cs102groupproject.Server;
import cs102groupproject.SharedObjects.*;
/**
 * Responsible for sending notifications to clients. Used to deliver server-side updates asynchronously.
 * Authors: Begüm Göktaş, Delfin Eryılmaz
 * Date: 27/12/2025
 */
public class NotificationService {
    /**
     * Sends friends request to the user specified with the reference id from user id.
     * @param userID
     * @param referenceID
     * @param notificationText
     */
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

    /**
     * Sends PDF request notification to the user.
     * @param userID
     * @param referenceID
     * @param notification
     */
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

    /**
     * Sends the informative notification to the user (Session ending, earning currency etc.)
     * @param userID
     * @param notificationText
     * @param heading
     * @return
     */
    public static Notification sendInfoNotification(int userID, String notificationText, String heading) {
        Notification notif = new Notification(userID, -1, notificationText, heading, null, Notification.Type.INFORMATIVE);
        return notif;
    }
}
