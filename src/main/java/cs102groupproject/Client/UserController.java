package cs102groupproject.Client;

import cs102groupproject.SharedObjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UserController {
    SessionManager manager;
    @FXML
    private TitledPane userInfo;
    @FXML
    private TitledPane sessionStats;
    @FXML
    private TitledPane habitStats;

    public UserController(SessionManager manager) {
        this.manager = manager;
    }

    public User getCurrentUser() {return manager.getCurrentUser();}
    public String getCurrentSessionID() {return manager.getSessionID();}

    @FXML
    public void displayInfo()
    {
        VBox userInfoContent = new VBox();

        int avatar = manager.getCurrentUser().getAvatar().charAt(0);
        Label avatarLabel = new Label((char)avatar + "");
        Label usernameLabel = new Label("Username: " + manager.getCurrentUser().getUsername());
        Label departmentLabel = new Label("Department: " + manager.getCurrentUser().getDepartment());
        Label emailLabel = new Label("Email: " + manager.getCurrentUser().getCredentials().getEmail());
        userInfoContent.getChildren().addAll(avatarLabel, usernameLabel, departmentLabel, emailLabel);

        Button editButton = new Button ("Edit Info");
        editButton.setOnAction(e -> {
            VBox box1 = new VBox();
            VBox box2 = new VBox();
            VBox box3 = new VBox();
        });
        userInfo.setContent(userInfoContent);
        Label label = new Label();
    }
}
