package cs102groupproject.Client;

import cs102groupproject.SharedObjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import cs102groupproject.SharedObjects.Habit;

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
        displayUserInfo();
        displaySessionStats();
        displayHabitStats();
    }

    public User getCurrentUser() {return manager.getCurrentUser();}
    public String getCurrentSessionID() {return manager.getSessionID();}

    @FXML
    private void displayUserInfo()
    {
        HBox userInfoContent = new HBox();

        String avatar = manager.getCurrentUser().getAvatar();
        Label avatarLabel = new Label(avatar);
        Label usernameLabel = new Label("Username: " + manager.getCurrentUser().getUsername());
        Label departmentLabel = new Label("Department: " + manager.getCurrentUser().getDepartment());
        Label emailLabel = new Label("Email: " + manager.getCurrentUser().getEmail().getEmail());
        HBox infoBox = new HBox(usernameLabel, departmentLabel, emailLabel);
        userInfoContent.getChildren().addAll(avatarLabel, infoBox);

        Button editButton = new Button ("Edit Info");
        userInfoContent.getChildren().add(editButton);
        editButton.setOnAction(e -> {
            VBox usernameBox = new VBox(new Label("New Username: "), new TextField());
            VBox departmentBox = new VBox(new Label("New Department: "), new TextField());
            VBox emailBox = new VBox(new Label("New Email: "), new TextField());
            HBox editBox = new HBox(usernameBox, departmentBox, emailBox);
            infoBox.getChildren().set(1, editBox);
            Button saveChangesButton = new Button ("Save Changes");
            userInfoContent.getChildren().set(userInfoContent.getChildren().indexOf(editButton), saveChangesButton);
            saveChangesButton.setOnAction(event -> {
                if (!((TextField)usernameBox.getChildren().get(1)).getText().isEmpty())
                {
                    manager.getCurrentUser().setUsername(((TextField)usernameBox.getChildren().get(1)).getText());
                }
                if (!((TextField)departmentBox.getChildren().get(1)).getText().isEmpty())
                {
                    manager.getCurrentUser().setDepartment(((TextField)departmentBox.getChildren().get(1)).getText());
                }
                if (!((TextField)emailBox.getChildren().get(1)).getText().isEmpty())
                {
                    manager.getCurrentUser().getEmail().setEmail(((TextField)emailBox.getChildren().get(1)).getText());
                }
                infoBox.getChildren().set(1, infoBox);
                userInfoContent.getChildren().set(userInfoContent.getChildren().indexOf(saveChangesButton), editButton);
            });
        });

        VBox oldPasswordBox = new VBox(new Label("Old Password: "), new TextField());
        VBox newPasswordBox = new VBox(new Label("New Password: "), new TextField());
        Button changePasswordButton = new Button ("Change Password");
        changePasswordButton.setOnAction(e -> {
            String oldPasswordInput = ((TextField)oldPasswordBox.getChildren().get(1)).getText();
            String newPasswordInput = ((TextField)newPasswordBox.getChildren().get(1)).getText();
            if (oldPasswordInput.equals(manager.getCurrentUser().getEmail().getPassword()) && !newPasswordInput.isEmpty())
            {
                manager.getCurrentUser().getEmail().setPassword(newPasswordInput);
            }
        });
        HBox passwordBox = new HBox(oldPasswordBox, newPasswordBox, changePasswordButton);
        userInfoContent.getChildren().add(passwordBox);

        userInfo.setContent(userInfoContent);
    }

    @FXML
    private void displaySessionStats()
    {
        Label sessionsCompletedLabel = new Label("Sessions Completed: ");
        Label individualSessionsLabel = new Label("Individual Sessions: " + String.valueOf(manager.getCurrentUser().getIndividualSessionsCompleted()));
        Label groupSessionsLabel = new Label("Group Sessions: " + String.valueOf(manager.getCurrentUser().getGroupSessionsCompleted()));
        Label totalMinutesLabel = new Label("Total Minutes Spent: " + String.valueOf(manager.getCurrentUser().getTotalMinutesSpent()));
        HBox sessionStatsContent = new HBox(sessionsCompletedLabel, individualSessionsLabel, groupSessionsLabel, totalMinutesLabel);
        sessionStats.setContent(sessionStatsContent);
    }

    @FXML
    private void displayHabitStats()
    {
        ScrollPane habitStats = new ScrollPane();
        Label habitStatsLabel = new Label("Completed Habits: ");
        HBox habitStatsContent = new HBox();
        for (Habit habit : manager.getCurrentUser().getHabitsCompleted())
        {
            Label habitLabel = new Label(habit.getName() + ": " + habit.calculateCompletionCount() + "/30");
            habitStatsContent.getChildren().add(habitLabel);
        }
        habitStats.setContent(habitStatsContent);
        HBox habitStatsTotalContent = new HBox(habitStatsLabel, habitStats);
        habitStats.setContent(habitStatsTotalContent);
    }
}
