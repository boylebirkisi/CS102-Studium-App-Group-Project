package cs102groupproject.Client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import cs102groupproject.App;
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
            ChatMessage incomingMsg = (ChatMessage) payload;
            Platform.runLater(() -> {
                chatMessages.add(incomingMsg);
                
                // Eğer şu an mesajı gönderen kişiyle chat sayfamız açıksa ekrana bas
                if (friendNameText.getText().equals(getUserNameFromId(incomingMsg.getSenderID()))) {
                    renderMessage(incomingMsg, false); // Sola yasla (sender karşı taraf)
                } else {
                    System.out.println("Yeni mesaj geldi: " + incomingMsg.getMessage());
                }
            });
        });

        // Böyle data alınacak
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

    public void setFields(ArrayList<User> friendsList, ArrayList<User> onlineUsers,
        ArrayList<ChatMessage> chatMessages, ArrayList<User> allUsers) {
        this.friendsList = friendsList;
        this.onlineUsers = onlineUsers;
        this.chatMessages = chatMessages;
        this.allUsers = allUsers;
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
        currencyEarnedLabel.setText("You have earned " + ClientSession.getCurrentUser().getGroupCurrency() + " so far. Study more, earn more.");
    }

    @FXML
    private void createGroupSessionButtonFunctionality() throws IOException
    {
        showPopUp();
    }

    @FXML
    private void searchUsers()
    {
        String name = userNameTextField.getText();
        String department = departmentTextField.getText();
        ArrayList<User> allUsers = new ArrayList<>(onlineUsers);
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
        friendsVBox.getChildren().clear();
        for (User friend : friendsList)
        {
            HBox friendBox = createUserDisplayBox(friend, true);
            friendsVBox.getChildren().add(friendBox);
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
        sessionBox.setOnMouseClicked(e ->
            {
                Circle requestSentCircle = new Circle(5, Color.GREEN);
                sessionBox.getChildren().add(requestSentCircle);
                //send server join request.
            }
        );
        return sessionBox;
    }

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

    private HBox createUserDisplayBox(User user, boolean isFriend)
    {
        Label avatarLabel = new Label(user.getAvatar());
        Label idLabel = new Label(user.getId() + "");
        idLabel.setManaged(false);
        idLabel.setVisible(false);
        Label userLabel = new Label(user.getUsername() + " / " + user.getDepartment());
        HBox userBox = new HBox(idLabel, avatarLabel, userLabel);
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
                System.out.println("currentuserid: " + ClientSession.getUserId() + " chat with userid: " + user.getId());
                chatVBox.getChildren().clear();
                toggleOnChatMenu();
                friendNameText.setText(user.getUsername());
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
                        //renderMessage(newMessage, true); // Mesajı sağa yasla (sender biziz)
                        Label messageTextNode = new Label(newMessage.getMessage());
                        LocalDateTime timestamp = newMessage.getTimestamp();
                        Label messageTime = new Label(timestamp.getHour() + ":" + timestamp.getMinute());
                        HBox messageBox = new HBox(messageTextNode, messageTime);
                        messageBox.setAlignment(Pos.CENTER_RIGHT);
                        chatVBox.getChildren().add(messageBox);
                        sendMessageTextField.clear();
                    }
                );
                for (ChatMessage msg : chatMessages)
                {
                    if (msg.getSenderID() == user.getId() || msg.getReceiverID() == user.getId()) {
                        boolean isMine = (msg.getSenderID() == ClientSession.getUserId());
                        //renderMessage(msg, isMine);
                    }
                    if ((msg.getSenderID() == user.getId() && msg.getReceiverID() == ClientSession.getUserId()) ||
                        (msg.getReceiverID() == user.getId() && msg.getSenderID() == ClientSession.getUserId()))
                    {
                        Label messageText = new Label(msg.getMessage());
                        LocalDateTime timestamp = msg.getTimestamp();
                        Label messageTime = new Label(timestamp.getHour() + ":" + timestamp.getMinute());
                        HBox messageBox = new HBox(messageText, messageTime);
                        if (msg.getSenderID() == user.getId())
                        {
                            messageBox.setAlignment(Pos.CENTER_LEFT);
                        }
                        else
                        {
                            messageBox.setAlignment(Pos.CENTER_RIGHT);
                        }
                        chatVBox.getChildren().add(messageBox);
                        sendMessageTextField.clear();
                    }
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
            //Call method to visit user's office
        });
        userBox.setSpacing(10);
        return userBox;
    }

    private void renderMessage(ChatMessage msg, boolean isMine) {
        Label textLabel = new Label(msg.getMessage());
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(200);
        
        // Basit bir stil: Bizimkiler mavi, gelenler gri olsun
        String style = isMine ? "-fx-background-color: #0084ff; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 8;" 
                            : "-fx-background-color: #e4e6eb; -fx-text-fill: black; -fx-background-radius: 10; -fx-padding: 8;";
        textLabel.setStyle(style);

        Label timeLabel = new Label(msg.getTimestamp().getHour() + ":" + msg.getTimestamp().getMinute());
        timeLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: gray;");

        VBox bubble = new VBox(textLabel, timeLabel);
        HBox messageBox = new HBox(bubble);
        
        if (isMine) {
            messageBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageBox.setAlignment(Pos.CENTER_LEFT);
        }
        
        chatVBox.getChildren().add(messageBox);
        // Otomatik aşağı kaydır
        chatScrollPane.setVvalue(1.0);
    }

    @FXML
    private void handleGoBackButton()
    {
        toggleOnFriendsList();
    }

    private void toggleOnFriendsList()
    {
        toggleFriendMenuVsChatMenu(true);
    }

    private void toggleOnChatMenu()
    {
        toggleFriendMenuVsChatMenu(false);
    }

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

    public void showPopUp() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GroupSessionPlannerPopUp.fxml"));
        GroupSessionPlannerPopUpController controller = loader.getController();
        controller.setOwnerController(this);
        Parent popUpRoot = loader.load();
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

    public void closePopUp()
    {
        popUpStage.close();
    }

    @FXML
    public void refreshButtonFunctionality() {
        ClientSession.login(onlineUsers.get(1)); // Dummy login for testing
        refreshUI();
    }

    private String getUserNameFromId(int id) {
        for (User u : friendsList) {
            if (u.getId() == id) return u.getUsername();
        }
        return "Unknown";
    }
}
