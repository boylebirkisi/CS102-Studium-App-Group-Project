package cs102groupproject.Server;
import cs102groupproject.SharedObjects.*;
/**
 * Responsible for sending notifications to clients. Used to deliver server-side updates asynchronously.
 */
public class NotificationService {
    public static void sendFriendRequest(int userID, int referenceID, String notification) {
        Notification notif = new Notification(userID, referenceID, "wants to be your friend!", "New Friend Request", null, Notification.Type.FRIEND_REQUEST);
        OptionalNotification optNotif = new OptionalNotification(
            userID,
            referenceID,
            notification,
            "New Friend Request",
            null,
            "Accept",
            "Decline",
            Notification.Type.FRIEND_REQUEST
        );
    }
}
