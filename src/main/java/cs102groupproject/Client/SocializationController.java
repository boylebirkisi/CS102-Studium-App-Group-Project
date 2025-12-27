package cs102groupproject.Client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.ChatMessage;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.Session;
import cs102groupproject.SharedObjects.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * Controller for the socialization UI.
 * @author Ali Mersin / Begüm Göktaş(partially)
 */
public class SocializationController implements UIController {
    Stage popUpStage = null;
    ArrayList<User> friendsList;
    ArrayList<User> onlineUsers;
    ArrayList<ChatMessage> chatMessages;
    ArrayList<User> allUsers;
    @FXML
    private Text currencyEarnedLabel;
    @FXML
    private Button searchUsersButton;
    @FXML
    private Button searchSessionsButton;
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
    @FXML
    private VBox friendsTotalVBox;
    @FXML
    private Text friendsText;
    @FXML
    private Button goBackButton;
    @FXML
    private Text friendNameText;
    @FXML
    private ScrollPane friendsScrollPane;
    @FXML
    private VBox chatVBox;
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private TextField sendMessageTextField;
    @FXML
    private HBox friendNameTextHBox;

    public ArrayList<User> getFriendsList() {return friendsList;}
    public void addFriend(User friend) {friendsList.add(friend);}
    public void removeFriend(User friend) {friendsList.remove(friend);}

    @FXML
    private void initialize()
    {
        this.allUsers = new ArrayList<>();
        this.friendsList = new ArrayList<>();
        this.onlineUsers = new ArrayList<>();
        this.chatMessages = new ArrayList<>();

        userNameTextField.setOnAction(e -> searchUsers());
        departmentTextField.setOnAction(e -> searchUsers());
        allUsers.add(new User(99, "testuser", "CS", "Avatar", "true", true, ""));
        WebSocketClient.addListener(ActionType.ERROR, (payload) -> {
            String errorMessage = (String) payload;
            
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error is detected.");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            });
        });
        
        WebSocketClient.addListener(ActionType.RECEIVE_PRIVATE_MESSAGE, (payload) -> {
            // Güvenli dönüşüm
            String json = WebSocketClient.getGson().toJson(payload);
            ChatMessage incomingMsg = WebSocketClient.getGson().fromJson(json, ChatMessage.class);
            
            Platform.runLater(() -> {
                if (chatMessages == null) chatMessages = new ArrayList<>();
                chatMessages.add(incomingMsg);
                
                if (friendNameText.isVisible() && getUserNameFromId(incomingMsg.getSenderID()).equals(friendNameText.getText())) {
                    renderMessage(incomingMsg, false);
                } else {
                    System.out.println("new message arrived" + incomingMsg.getMessage());
                }
            });
        });

        WebSocketClient.addListener(ActionType.FRIEND_ADDED, (payload) -> {
            String json = WebSocketClient.getGson().toJson(payload);
            User newFriend = WebSocketClient.getGson().fromJson(json, User.class);
            
            Platform.runLater(() -> {
                if (!friendsList.contains(newFriend)) {
                    friendsList.add(newFriend);
                    refreshUI(); // Listeyi güncelle
                }
            });
        });

        //this.friendsList = new ArrayList<>(ClientSession.getLoginResponse().getFriends());
        //this.chatMessages = new ArrayList<>(ClientSession.getLoginResponse().getMessages());
        
        //friendsList = new ArrayList<>();
        //User friend = new User(1, "Alice", "Computer Science", "A", "true", true, "String");
        //friendsList.add(friend);
        //onlineUsers = new ArrayList<>();
        //onlineUsers.add(friend);
        //friend.addCurrency(500, false);
        //friend.addCurrency(200, true);
        //chatMessages = new ArrayList<>();
        //User notFriend = new User(2, "Bob", "Mathematics", "B", "true", true, "String");
        //onlineUsers.add(notFriend);
        //User anotherNotFriend = new User(3, "Charlie", "Physics", "C", "true", true, "String");
        //onlineUsers.add(anotherNotFriend);
    }

    /**
     * Sets the fields of the SocializationController.
     * @param friendsList The list of friends
     * @param onlineUsers The list of online users
     * @param chatMessages The list of all chat messages of all users
     * @param allUsers The list of all users
     */
    public void setFields(ArrayList<User> friendsList, ArrayList<User> onlineUsers,
        ArrayList<ChatMessage> chatMessages, ArrayList<User> allUsers) {
        this.friendsList = friendsList;
        this.onlineUsers = onlineUsers;
        this.chatMessages = chatMessages;
        this.allUsers = allUsers;
        if (onlineUsers == null) {
            this.onlineUsers = new ArrayList<>();
        }
        if (chatMessages == null) {
            this.chatMessages = new ArrayList<>();
        }
        if (allUsers == null) {
            this.allUsers = new ArrayList<>();
        }
        if (friendsList == null) {
            this.friendsList = new ArrayList<>();
        }
        refreshUI();
    }

    // Refreshes the UI elements
    private void refreshUI() {
        displayCurrencyEarned();
        listFriends();
        displayOnlineFriends();
    }

    @FXML
    private void displayCurrencyEarned()
    {
        currencyEarnedLabel.setText("You have earned " + ClientSession.getCurrentUser().getGroupCurrency() + " so far. Study more, earn more.");
    }

    @FXML
    private void createGroupSessionButtonFunctionality() throws IOException
    {
        showPopUp();
    }

    // Search users button action
    @FXML
    private void searchUsers() {
        String name = userNameTextField.getText().toLowerCase().trim();
        
        searchResultsHBox.getChildren().clear();
        VBox leftCol = new VBox(10);
        VBox rightCol = new VBox(10);
        searchResultsHBox.getChildren().addAll(leftCol, rightCol);

        if (allUsers == null || allUsers.isEmpty()) {
            System.out.println("Error: no user is here to be shown");
            return;
        }

        int count = 0;
        for (User user : allUsers) {
            if (user.getId() == ClientSession.getUserId() || friendsList.contains(user)) {
                continue;
            }

            if (name.isEmpty() || user.getUsername().toLowerCase().contains(name)) {
                HBox userBox = createUserDisplayBox(user, false);
                if (count % 2 == 0) leftCol.getChildren().add(userBox);
                else rightCol.getChildren().add(userBox);
                count++;
            }
        }
    }

    // Get sessions button action
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

    // List friends in the friends list
    @FXML
    private void listFriends()
    {
        friendsVBox.getChildren().clear();
        for (User friend : friendsList)
        {
            HBox friendBox = createUserDisplayBox(friend, true);
            friendsVBox.getChildren().add(friendBox);
        }
    }

    /**
     * Filters sessions by name and status.
     * @param name The name to filter by
     * @param aboutToStart Whether to include sessions that are about to start
     * @param hasStarted Whether to include sessions that have started
     * @param allSessions The list of all sessions to filter
     * @return A list of sessions filtered by the given criteria
     */
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

    /**
     * Creates a session display box for a given session.
     * @param session The session to create a display box for
     * @return An HBox containing the session display
     */
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
        sessionBox.setOnMouseClicked(e ->
            {
                Circle requestSentCircle = new Circle(5, Color.GREEN);
                sessionBox.getChildren().add(requestSentCircle);
                //send server join request.
            }
        );
        return sessionBox;
    }

    /**
     * Filters users by name and department.
     * @param name The name to filter by
     * @param department The department to filter by
     * @param allUsers The list of all users to filter
     * @return An array list of users filtered by the given criteria
     */
    private ArrayList<User> filterUsersByNameAndDepartment(String name, String department, ArrayList<User> allUsers)
    {
        
        for (int i = 0; i < allUsers.size(); i++)
        {
            User user = allUsers.get(i);
            if (user.getId() == ClientSession.getUserId() || friendsList.contains(user))
            {
                allUsers.remove(user);
                i--;
            }
        }
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

    /**
     * Creates a user display box for a given user.
     * @param user The user to create a display box for
     * @param isFriend Whether the user is a friend
     * @return An HBox containing the user display
     */
    private HBox createUserDisplayBox(User user, boolean isFriend)
    {
        Label avatarLabel = new Label(user.getAvatar());
        Label idLabel = new Label(user.getId() + "");
        idLabel.setManaged(false);
        idLabel.setVisible(false);
        Label userLabel = new Label(user.getUsername() + " / " + user.getDepartment());
        HBox userBox = new HBox(idLabel, avatarLabel, userLabel);
        if (!isFriend) {
                Button addFriendButton = new Button("Add Friend");
                addFriendButton.setOnAction(e -> {
                    WebSocketClient.send(new ProtocolMessage(ActionType.ADD_FRIEND, user));
                    addFriendButton.setDisable(true);
                    addFriendButton.setText("Request Sent");
                });
                userBox.getChildren().add(addFriendButton);
        }
        else
        {
            Button sendMessageButton = new Button("Send Message");
            sendMessageButton.onMouseClickedProperty().set(e -> {
                System.out.println("currentuserid: " + ClientSession.getUserId() + " chat with userid: " + user.getId());
                chatVBox.getChildren().clear();
                toggleOnChatMenu();
                friendNameText.setText(user.getUsername());
                
                sendMessageTextField.setOnAction(null);
                // chatVBox.getChildren().clear();
                // toggleOnChatMenu();
                // friendNameText.setText(user.getUsername());
                sendMessageTextField.setOnAction(v ->
                    {
                        String messageText = sendMessageTextField.getText();
                        if (messageText.isEmpty())
                        {
                            return;
                        }
                        ChatMessage newMessage = new ChatMessage(ClientSession.getUserId(), user.getId(), messageText);
                        WebSocketClient.send(new ProtocolMessage(ActionType.SEND_PRIVATE_MESSAGE, newMessage));
                        chatMessages.add(newMessage);
                        renderMessage(newMessage, true);
                        // Label messageTextNode = new Label(newMessage.getMessage());
                        // LocalDateTime timestamp = newMessage.getTimestamp();
                        // Label messageTime = new Label(timestamp.getHour() + ":" + timestamp.getMinute());
                        // HBox messageBox = new HBox(messageTextNode, messageTime);
                        // messageBox.setAlignment(Pos.CENTER_RIGHT);
                        // chatVBox.getChildren().add(messageBox);
                        sendMessageTextField.clear();
                    }
                );
                for (ChatMessage msg : chatMessages)
                {
                    if ((msg.getSenderID() == user.getId() && msg.getReceiverID() == ClientSession.getUserId()) ||
                                (msg.getReceiverID() == user.getId() && msg.getSenderID() == ClientSession.getUserId())) {
                                renderMessage(msg, msg.getSenderID() == ClientSession.getUserId());
                            }
                    // if ((msg.getSenderID() == user.getId() && msg.getReceiverID() == ClientSession.getUserId()) ||
                    //     (msg.getReceiverID() == user.getId() && msg.getSenderID() == ClientSession.getUserId()))
                    // {
                    //     Label messageText = new Label(msg.getMessage());
                    //     LocalDateTime timestamp = msg.getTimestamp();
                    //     Label messageTime = new Label(timestamp.getHour() + ":" + timestamp.getMinute());
                    //     HBox messageBox = new HBox(messageText, messageTime);
                    //     if (msg.getSenderID() == user.getId())
                    //     {
                    //         messageBox.setAlignment(Pos.CENTER_LEFT);
                    //     }
                    //     else
                    //     {
                    //         messageBox.setAlignment(Pos.CENTER_RIGHT);
                    //     }
                    //     chatVBox.getChildren().add(messageBox);
                    //     sendMessageTextField.clear();
                    // }
                }
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
            //Call method to visit user's office (it is not implemented)
        });
        userBox.setSpacing(10);
        return userBox;
    }

    /**
     * Renders a chat message in the chat UI.
     * @param msg The chat message to render
     * @param isMine Whether the message was sent by the current user
     */
    private void renderMessage(ChatMessage msg, boolean isMine) {
        Label textLabel = new Label(msg.getMessage());
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(250);
        
        String color = isMine ? "#0084ff" : "#e4e6eb";
        String textColor = isMine ? "white" : "black";
        textLabel.setStyle("-fx-background-color: " + color + "; " +
                        "-fx-text-fill: " + textColor + "; " +
                        "-fx-background-radius: 15; -fx-padding: 10; -fx-font-size: 13px;");

        Label timeLabel = new Label(msg.getTimestamp().getHour() + ":" + String.format("%02d", msg.getTimestamp().getMinute()));
        timeLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: gray;");

        VBox bubble = new VBox(textLabel, timeLabel);
        bubble.setSpacing(2);
        
        HBox messageRow = new HBox(bubble);
        messageRow.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
        messageRow.setAlignment(isMine ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        
        chatVBox.getChildren().add(messageRow);
        
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

    // Go back button action
    @FXML
    private void handleGoBackButton()
    {
        toggleOnFriendsList();
    }

    // Toggle friends menu instead of chat menu
    private void toggleOnFriendsList()
    {
        toggleFriendMenuVsChatMenu(true);
    }

    // Toggle chat menu instead of friends menu
    private void toggleOnChatMenu()
    {
        toggleFriendMenuVsChatMenu(false);
    }

    /**
     * Toggles between the friend menu and chat menu.
     * @param state If true, shows the friend menu. If false, shows the chat menu
     */
    private void toggleFriendMenuVsChatMenu(boolean state)
    {
        friendsScrollPane.setVisible(state);
        friendsScrollPane.setManaged(state);
        friendsText.setVisible(state);
        friendsText.setManaged(state);
        friendNameTextHBox.setVisible(!state);
        friendNameTextHBox.setManaged(!state);
        goBackButton.setVisible(!state);
        goBackButton.setManaged(!state);
        chatScrollPane.setVisible(!state);
        chatScrollPane.setManaged(!state);
    }

    // Display online friends in the online friends section
    @FXML
    private void displayOnlineFriends()
    {
        onlineFriendsHBox.getChildren().clear();
        ArrayList<User> usersToDisplay = new ArrayList<>();
        for (User friend : friendsList)
        {
            if (onlineUsers.contains(friend))
            {
                usersToDisplay.add(friend);
            }
        }
        for (User onlineFriend: usersToDisplay)
        {
            Label avatarLabel = new Label(onlineFriend.getAvatar());
            onlineFriendsHBox.getChildren().add(avatarLabel);
        }
        onlineFriendsHBox.setSpacing(10);
    }

    /**
     * Shows the group session planner popup.
     * @throws IOException if the FXML file cannot be loaded
     */
    public void showPopUp() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GroupSessionPlannerPopUp.fxml"));
        Parent popUpRoot = loader.load();
        GroupSessionPlannerPopUpController controller = loader.getController();
        controller.setOwnerController(this);
        Scene scene = new Scene(popUpRoot);
        popUpStage = new Stage();
        popUpStage.setScene(scene);
        popUpStage.setTitle("Group Session Planner");
        popUpStage.initStyle(StageStyle.UTILITY);
        popUpStage.initModality(Modality.WINDOW_MODAL);
        popUpStage.initOwner(searchSessionsButton.getScene().getWindow());
        popUpStage.show();
        popUpStage.setResizable(false);
        popUpStage.centerOnScreen();
    }

    // Close the popup window
    public void closePopUp()
    {
        popUpStage.close();
    }

    // Refresh button action
    @FXML
    public void refreshButtonFunctionality() {
        refreshUI();
    }

    /**
     * Gets the username from a user ID.
     * @param id The user ID
     * @return The username for the given user ID
     */
    private String getUserNameFromId(int id) {
        for (User u : friendsList) {
            if (u.getId() == id) return u.getUsername();
        }
        return "Unknown";
    }
}
