package cs102groupproject.Client;

import java.util.ArrayList;

import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.GroupSession;
import cs102groupproject.SharedObjects.Notification;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.User;
import cs102groupproject.SharedObjects.Notification.Status;
import cs102groupproject.SharedObjects.Notification.Type;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NotificationsController {
    ArrayList<Notification> notifications;
    ArrayList<User> users;
    ArrayList<GroupSession> groupSessions;
    @FXML
    private VBox notificationsVBox;

    @FXML
    private void initialize()
    {
    }

    public void setFields(ArrayList<Notification> notifications)
    {
        this.notifications = notifications;
        drawNotifications();
    }

    private void drawNotifications()
    {
        notificationsVBox.getChildren().clear();
        for (Notification notification : notifications)
        {
            if (notification.getIsRemoved())
                {continue;}
            HBox notificationSubBox = new HBox(new Label(notification.getNotificationText()));
            VBox notificationBox = new VBox(new Label(notification.getType().toString()), notificationSubBox);
            if (notification.getType() == Type.FRIEND_REQUEST)
            {
                int otherUserID = notification.getReferenceID();
                User otherUser = null;
                for (User user : users)
                {
                    if (user.getId() == otherUserID)
                    {
                        otherUser = user;
                        notificationSubBox.getChildren().add(0, new Label(user.getAvatar().toCharArray()[0] + ""));
                        break;
                    }
                }
                Button acceptButton = new Button("Accept");
                acceptButton.setOnAction(e -> {
                    // send accept friend request to server
                    notification.setStatus(Status.ACCEPTED);
                    notification.removeNotification();
                    drawNotifications();
                });
                Button declineButton = new Button("Decline");
                declineButton.setOnAction(e -> {
                    // send reject friend request to server
                    notification.setStatus(Status.DECLINED);
                    notification.removeNotification();
                    drawNotifications();
                });
                VBox buttonBox = new VBox(acceptButton, declineButton);
                notificationSubBox.getChildren().add(buttonBox);
            }
            else if (notification.getType() == Type.SESSION_INVITE)
            {
                int sessionID = notification.getReferenceID();
                GroupSession otherSession = null;
                for (GroupSession session : groupSessions)
                {
                    if (session.getId() == sessionID)
                    {
                        otherSession = session;
                        break;
                    }
                }
                Button acceptButton = new Button("Accept");
                acceptButton.setOnAction(e -> {
                    // send accept friend request to server
                    notification.setStatus(Status.ACCEPTED);
                    notification.removeNotification();
                    drawNotifications();
                });
                Button declineButton = new Button("Decline");
                declineButton.setOnAction(e -> {
                    // send reject friend request to server
                    notification.setStatus(Status.DECLINED);
                    notification.removeNotification();
                    drawNotifications();
                });
                VBox buttonBox = new VBox(acceptButton, declineButton);
                notificationSubBox.getChildren().add(buttonBox);
            }
            else if (notification.getType() == Type.SESSION_ACCESS_REQUEST)
            {
                int userID = notification.getReferenceID();
                User otherUser = null;
                for (User user : users)
                {
                    if (user.getId() == userID)
                    {
                        otherUser = user;
                        break;
                    }
                }
                Button acceptButton = new Button("Accept");
                acceptButton.setOnAction(e -> {
                    // send accept friend request to server
                    notification.setStatus(Status.ACCEPTED);
                    notification.removeNotification();
                    drawNotifications();
                });
                Button declineButton = new Button("Decline");
                declineButton.setOnAction(e -> {
                    // send reject friend request to server
                    notification.setStatus(Status.DECLINED);
                    notification.removeNotification();
                    drawNotifications();
                });
                VBox buttonBox = new VBox(acceptButton, declineButton);
                notificationSubBox.getChildren().add(buttonBox);
            }
            else if (notification.getType() == Type.PDF_REQUEST)
            {
                int userID = notification.getReferenceID();
                User otherUser = null;
                for (User user : users)
                {
                    if (user.getId() == userID)
                    {
                        otherUser = user;
                        break;
                    }
                }
                Button acceptButton = new Button("Accept");
                acceptButton.setOnAction(e -> {
                    // send accept friend request to server
                    notification.setStatus(Status.ACCEPTED);
                    notification.removeNotification();
                    drawNotifications();
                });
                Button declineButton = new Button("Decline");
                declineButton.setOnAction(e -> {
                    // send reject friend request to server
                    notification.setStatus(Status.DECLINED);
                    notification.removeNotification();
                    drawNotifications();
                });
                VBox buttonBox = new VBox(acceptButton, declineButton);
                notificationSubBox.getChildren().add(buttonBox);
            }
            else if (notification.getType() == Type.INFORMATIVE)
            {
                Button okButton = new Button("OK");
                okButton.setOnAction(e -> {
                    // send accept friend request to server
                    notification.setStatus(Status.ACCEPTED);
                    notification.removeNotification();
                    drawNotifications();
                });
                notificationSubBox.getChildren().add(okButton);
            }
            notificationsVBox.getChildren().add(notificationBox);
        }
    }
}
