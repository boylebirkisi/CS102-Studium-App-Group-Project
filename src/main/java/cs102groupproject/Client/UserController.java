package cs102groupproject.Client;

import cs102groupproject.SharedObjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
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

    @FXML
    private void initialize()
    {
    }

    public void setSessionManager(SessionManager manager)
    {
        this.manager = manager;
        refreshUI();
    }

    private void refreshUI()
    {
        // displayUserInfo();
        displaySessionStats();
        displayHabitStats();
    }

    public User getCurrentUser() {return manager.getCurrentUser();}
    public int getCurrentSessionID() {return manager.getSessionID();}

    // @FXML
    // private void displayUserInfo()
    // {
    //     VBox userInfoContent = new VBox();

    //     String avatar = manager.getCurrentUser().getAvatar();
    //     Label avatarLabel = new Label(avatar);
    //     Label usernameLabel = new Label("Username: " + manager.getCurrentUser().getUsername());
    //     Label departmentLabel = new Label("Department: " + manager.getCurrentUser().getDepartment());
    //     Label emailLabel = new Label("Email: " + manager.getCurrentUser().getEmail().getEmail());
    //     VBox infoBox = new VBox(usernameLabel, departmentLabel, emailLabel);
    //     userInfoContent.getChildren().addAll(avatarLabel, infoBox);

    //     Button editButton = new Button ("Edit Info");
    //     userInfoContent.getChildren().add(editButton);
    //     editButton.setOnAction(e -> {
    //         HBox usernameBox = new HBox(new Label("New Username: "), new TextField());
    //         HBox departmentBox = new HBox(new Label("New Department: "), new TextField());
    //         HBox emailBox = new HBox(new Label("New Email: "), new TextField());
    //         VBox editBox = new VBox(usernameBox, departmentBox, emailBox);
    //         infoBox.getChildren().set(1, editBox);
    //         Button saveChangesButton = new Button ("Save Changes");
    //         userInfoContent.getChildren().set(userInfoContent.getChildren().indexOf(editButton), saveChangesButton);
    //         saveChangesButton.setOnAction(event -> {
    //             if (!((TextField)usernameBox.getChildren().get(1)).getText().isEmpty())
    //             {
    //                 manager.getCurrentUser().setUsername(((TextField)usernameBox.getChildren().get(1)).getText());
    //             }
    //             if (!((TextField)departmentBox.getChildren().get(1)).getText().isEmpty())
    //             {
    //                 manager.getCurrentUser().setDepartment(((TextField)departmentBox.getChildren().get(1)).getText());
    //             }
    //             if (!((TextField)emailBox.getChildren().get(1)).getText().isEmpty())
    //             {
    //                 manager.getCurrentUser().getEmail().setEmail(((TextField)emailBox.getChildren().get(1)).getText());
    //             }
    //             infoBox.getChildren().set(1, infoBox);
    //             userInfoContent.getChildren().set(userInfoContent.getChildren().indexOf(saveChangesButton), editButton);
    //         });
    //     });

    //     HBox oldPasswordBox = new HBox(new Label("Old Password: "), new TextField());
    //     HBox newPasswordBox = new HBox(new Label("New Password: "), new TextField());
    //     Button changePasswordButton = new Button ("Change Password");
    //     changePasswordButton.setOnAction(e -> {
    //         String oldPasswordInput = ((TextField)oldPasswordBox.getChildren().get(1)).getText();
    //         String newPasswordInput = ((TextField)newPasswordBox.getChildren().get(1)).getText();
    //         if (oldPasswordInput.equals(manager.getCurrentUser().getEmail().getPassword()) && !newPasswordInput.isEmpty())
    //         {
    //             manager.getCurrentUser().getEmail().setPassword(newPasswordInput);
    //         }
    //     });
    //     VBox passwordBox = new VBox(oldPasswordBox, newPasswordBox, changePasswordButton);
    //     userInfoContent.getChildren().add(passwordBox);

    //     userInfo.setContent(userInfoContent);
    // }

    @FXML
    private void displaySessionStats()
    {
        Label sessionsCompletedLabel = new Label("Sessions Completed: ");
        Label individualSessionsLabel = new Label("Individual Sessions: " + String.valueOf(manager.getCurrentUser().getIndividualSessionsCompleted()));
        Label groupSessionsLabel = new Label("Group Sessions: " + String.valueOf(manager.getCurrentUser().getGroupSessionsCompleted()));
        Label totalMinutesLabel = new Label("Total Minutes Spent: " + String.valueOf(manager.getCurrentUser().getTotalMinutesSpent()));
        VBox sessionStatsContent = new VBox(sessionsCompletedLabel, individualSessionsLabel, groupSessionsLabel, totalMinutesLabel);
        sessionStats.setContent(sessionStatsContent);
    }

    @FXML
    private void displayHabitStats()
    {
        ScrollPane habitStats = new ScrollPane();
        Label habitStatsLabel = new Label("Completed Habits: ");
        VBox habitStatsContent = new VBox();
        for (Habit habit : manager.getCurrentUser().getHabitsCompleted())
        {
            Label habitLabel = new Label(habit.getName() + ": " + habit.calculateCompletionCount() + "/30");
            habitStatsContent.getChildren().add(habitLabel);
        }
        habitStats.setContent(habitStatsContent);
        VBox habitStatsTotalContent = new VBox(habitStatsLabel, habitStats);
        habitStats.setContent(habitStatsTotalContent);
    }
}
