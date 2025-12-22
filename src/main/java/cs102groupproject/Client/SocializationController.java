package cs102groupproject.Client;

import java.time.LocalDateTime;
import java.util.ArrayList;

import cs102groupproject.SharedObjects.Session;
import cs102groupproject.SharedObjects.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SocializationController {
    SessionManager manager;
    ArrayList<User> friendsList;
    @FXML
    private Label currencyEarnedLabel;
    @FXML
    private Button searchUsersButton;
    @FXML
    private Button searchSessionsButton;
    @FXML
    private Button createGroupSessionButton;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField departmentTextField;
    @FXML
    private HBox searchResultsHBox;
    @FXML
    private HBox searchParameters;
    @FXML
    private HBox userSearchParameters;
    @FXML
    private HBox sessionSearchParameters;
    @FXML
    private CheckBox aboutToStartCheckBox;
    @FXML
    private CheckBox hasStartedCheckBox;
    @FXML
    private TextField sessionNameTextField;
    @FXML
    private VBox friendsVBox;
    @FXML
    private HBox onlineFriendsHBox;

    public ArrayList<User> getFriendsList() {return friendsList;}
    public void addFriend(User friend) {friendsList.add(friend);}
    public void removeFriend(User friend) {friendsList.remove(friend);}

    @FXML
    private void initialize()
    {
        friendsList = new ArrayList<>();
    }

    public void setSessionManager(SessionManager manager) {
        this.manager = manager;
        refreshUI();
    }

    private void refreshUI() {
        displayCurrencyEarned();
        listFriends();
        displayOnlineFriends();
    }

    @FXML
    private void displayCurrencyEarned()
    {
        currencyEarnedLabel.setText("You have earned " + manager.getCurrentUser().getGroupCurrency() + " so far. Study more, earn more.");
    }

    @FXML
    private void createGroupSessionButtonFunctionality()
    {
        // Open the group session creation pop-up window
    }

    @FXML
    private void searchUsers()
    {
        String name = userNameTextField.getText();
        String department = departmentTextField.getText();
        ArrayList<User> allUsers = new ArrayList<>();
        ArrayList<User> filteredUsers = filterUsersByNameAndDepartment(name, department, allUsers);
        for (Node node: searchParameters.getChildren())
        {
            node.setVisible(false);
            node.setManaged(false);
        }
        searchParameters.getChildren().get(searchParameters.getChildren().indexOf(userSearchParameters)).setVisible(true);
        searchParameters.getChildren().get(searchParameters.getChildren().indexOf(userSearchParameters)).setManaged(true);
        searchResultsHBox.getChildren().clear();
        searchResultsHBox.getChildren().addAll(new VBox(), new VBox());
        for (int i = 0; i < filteredUsers.size(); i++)
        {
            User user = filteredUsers.get(i);
            HBox userBox = createUserDisplayBox(user, false);
            ((VBox)searchResultsHBox.getChildren().get(i % 2)).getChildren().add(userBox);
        }
    }

    @FXML
    private void searchSessions()
    {
        String name = sessionNameTextField.getText();
        boolean aboutToStart = aboutToStartCheckBox.isSelected();
        boolean hasStarted = hasStartedCheckBox.isSelected();
        ArrayList<Session> allSessions = new ArrayList<>();
        ArrayList<Session> filteredSessions = filterSessionsByNameAndStatus(name, aboutToStart, hasStarted, allSessions);
        for (Node node: searchParameters.getChildren())
        {
            node.setVisible(false);
            node.setManaged(false);
        }
        searchParameters.getChildren().get(searchParameters.getChildren().indexOf(sessionSearchParameters)).setVisible(true);
        searchParameters.getChildren().get(searchParameters.getChildren().indexOf(sessionSearchParameters)).setManaged(true);
        searchResultsHBox.getChildren().clear();
        searchResultsHBox.getChildren().addAll(new VBox(), new VBox());
        for (int i = 0; i < filteredSessions.size(); i++)
        {
            Session session = filteredSessions.get(i);
            HBox sessionBox = createSessionDisplayBox(session);
            ((VBox)searchResultsHBox.getChildren().get(i % 2)).getChildren().add(sessionBox);
        }
    }

    @FXML
    private void listFriends()
    {
        for (User friend : friendsList)
        {
            HBox friendBox = createUserDisplayBox(friend, true);
            ((VBox)searchResultsHBox.getChildren().get(0)).getChildren().add(friendBox);
        }
    }

    private ArrayList<Session> filterSessionsByNameAndStatus(String name, boolean aboutToStart, boolean hasStarted, ArrayList<Session> allSessions)
    {
        ArrayList<Session> filteredSessions = new ArrayList<>();
        for (Session session : allSessions)
        {
            LocalDateTime sessionDate = session.getDate();
            LocalDateTime currentDate = LocalDateTime.now();
            int yearDiff = sessionDate.getYear() - currentDate.getYear();
            int dayDiff = sessionDate.getDayOfYear() - currentDate.getDayOfYear();
            int hourDiff = sessionDate.getHour() - currentDate.getHour();
            boolean isAboutToStart = false;
            boolean isOngoing = false;
            if (yearDiff == 0 && dayDiff == 0 && hourDiff <= 2)
            {
                isAboutToStart = sessionDate.isAfter(currentDate);
            }
            if (yearDiff == 0 && dayDiff == 0 && hourDiff <= 0 && !session.getIsCompleted())
            {
                isOngoing = true;
            }
            if ((isAboutToStart && aboutToStart) && (name.isEmpty() || session.getName().toLowerCase().contains(name.toLowerCase())) && (isOngoing && hasStarted))
            {
                filteredSessions.add(session);
            }
        }
        return filteredSessions;
    }

    private HBox createSessionDisplayBox(Session session)
    {
        Label sessionLabel = new Label(session.getName() + " / " + session.getType());
        Label lengthLabel = new Label(session.getNo() + " Session " + session.getLength() + "/" + session.getBreakLength());
        Label sessionStartInfoLabel = new Label();
        if (session.getDate().isAfter(LocalDateTime.now()))
        {
            sessionStartInfoLabel.setText("Starts at " + session.getDate().getHour() + ":" + session.getDate().getMinute());
        }
        else if (!session.getIsCompleted())
        {
            sessionStartInfoLabel.setText(session.getNo() + "/" + session.getTotalSeconds() / session.getLength() + session.getRemainingSeconds() / 60 + " minutes remaining.");
        }
        else
        {
            sessionStartInfoLabel.setText("Completed");
        }
        VBox sessionInfoBox = new VBox(lengthLabel, sessionStartInfoLabel);
        sessionInfoBox.setAlignment(Pos.CENTER_RIGHT);
        HBox sessionBox = new HBox(sessionLabel, sessionInfoBox);
        sessionBox.setSpacing(10);
        return sessionBox;
    }

    private ArrayList<User> filterUsersByNameAndDepartment(String name, String department, ArrayList<User> allUsers)
    {
        if (name.isEmpty() && department.isEmpty())
        {
            return allUsers;
        }
        else if (name.isEmpty())
        {
            ArrayList<User> filteredUsers = new ArrayList<>();
            for (User user : allUsers)
            {
                if (user.getDepartment().toLowerCase().contains(department.toLowerCase()))
                {
                    filteredUsers.add(user);
                }
            }
            return filteredUsers;
        }
        else if (department.isEmpty())
        {
            ArrayList<User> filteredUsers = new ArrayList<>();
            for (User user : allUsers)
            {
                if (user.getUsername().toLowerCase().contains(name.toLowerCase()))
                {
                    filteredUsers.add(user);
                }
            }
            return filteredUsers;
        }
        else
        {
            ArrayList<User> filteredUsers = new ArrayList<>();
            for (User user : allUsers)
            {
                if (user.getUsername().toLowerCase().contains(name.toLowerCase()) &&
                    user.getDepartment().toLowerCase().contains(department.toLowerCase()))
                {
                    filteredUsers.add(user);
                }
            }
            return filteredUsers;
        }
    }

    private HBox createUserDisplayBox(User user, boolean isFriend)
    {
        Label avatarLabel = new Label(user.getAvatar());
        Label userLabel = new Label(user.getUsername() + " / " + user.getDepartment());
        HBox userBox = new HBox(avatarLabel, userLabel);
        if (!isFriend)
        {
            Button addFriendButton = new Button("Add Friend");
            addFriendButton.onMouseClickedProperty().set(e -> {
                addFriend(user);
            });
            userBox.getChildren().add(addFriendButton);
        }
        else
        {
            Button sendMessageButton = new Button("Send Message");
            sendMessageButton.onMouseClickedProperty().set(e -> {
                //Open chat with friend
            });
            userBox.getChildren().add(sendMessageButton);
            ContextMenu contextMenu = new ContextMenu();
            MenuItem removeFriendItem = new MenuItem("Remove Friend");
            removeFriendItem.setOnAction(e ->
            {
                removeFriend(user);
                friendsVBox.getChildren().remove(userBox);
            });
            contextMenu.getItems().add(removeFriendItem);

            userBox.setOnContextMenuRequested(e ->
                contextMenu.show(userBox, e.getScreenX(), e.getScreenY())
            );
        }

        Button visitOfficeButton = new Button("Visit Office");
        visitOfficeButton.onMouseClickedProperty().set(e -> {
            //Call method to visit user's office
        });
        userBox.setSpacing(10);
        return userBox;
    }

    @FXML
    private void displayOnlineFriends()
    {
        ArrayList<User> onlineFriends = new ArrayList<>();
        for (User friend : friendsList)
        {
            if (friend.isOnline())
            {
                onlineFriends.add(friend);
            }
        }
        for (User onlineFriend: onlineFriends)
        {
            Label avatarLabel = new Label(onlineFriend.getAvatar());
            onlineFriendsHBox.getChildren().add(avatarLabel);
        }
        onlineFriendsHBox.setSpacing(10);
    }
}
