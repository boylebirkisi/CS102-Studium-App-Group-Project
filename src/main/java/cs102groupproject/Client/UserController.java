package cs102groupproject.Client;

import cs102groupproject.SharedObjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import cs102groupproject.SharedObjects.GroupSession;
import cs102groupproject.SharedObjects.Habit;
import cs102groupproject.SharedObjects.Session;

/**
 * Controller for the profile page UI.
 * @author Ali Mersin
 */
public class UserController implements UIController {
    ArrayList<Habit> habits;
    ArrayList<Session> sessions;
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

    /**
     * Sets the fields of the UserController.
     * @param habits
     * @param sessions
     */
    public void setFields(ArrayList<Habit> habits, ArrayList<Session> sessions)
    {
        this.habits = habits;
        this.sessions = sessions;
        refreshUI();
    }

    /**
     * Refreshes the UI elements
     */
    private void refreshUI()
    {
        // displayUserInfo();
        displaySessionStats();
        displayHabitStats();
    }

    public User getCurrentUser() {return ClientSession.getCurrentUser();}
    //public int getCurrentSessionID() {return manager.getSessionID();}

    // @FXML
    /**
     * Called when the dropdown menu for user info is opened
     */
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

    /**
     * Called when the dropdown menu for session stats is opened
     */
    @FXML
    private void displaySessionStats()
    {
        Label sessionsCompletedLabel = new Label("Sessions Completed: ");
        int soloSessionsCompleted = 0;
        int groupSessionsCompleted = 0;
        int totalMinutesSpent = 0;
        for (int i = 0; i < sessions.size(); i++)
        {
            Session session = sessions.get(i);
            if (session.getIsCompleted())
                if (session instanceof GroupSession)
                    groupSessionsCompleted++;
                else
                    soloSessionsCompleted++;
                totalMinutesSpent += (session.getTotalSeconds() - session.getRemainingSeconds()) / 60;
        }
        Label individualSessionsLabel = new Label("Individual Sessions: " + String.valueOf(soloSessionsCompleted));
        Label groupSessionsLabel = new Label("Group Sessions: " + String.valueOf(groupSessionsCompleted));
        Label totalMinutesLabel = new Label("Total Minutes Spent: " + String.valueOf(totalMinutesSpent));
        VBox sessionStatsContent = new VBox(sessionsCompletedLabel, individualSessionsLabel, groupSessionsLabel, totalMinutesLabel);
        sessionStats.setContent(sessionStatsContent);
    }

    /**
     * Called when the dropdown menu for habit stats is opened
    */
    @FXML
    private void displayHabitStats()
    {
        ScrollPane habitStats = new ScrollPane();
        Label habitStatsLabel = new Label("Completed Habits: ");
        VBox habitStatsContent = new VBox();
        for (Habit habit : habits)
        {
            Label habitLabel = new Label(habit.getName() + ": " + habit.calculateCompletionCount() + "/30");
            habitStatsContent.getChildren().add(habitLabel);
        }
        habitStats.setContent(habitStatsContent);
        VBox habitStatsTotalContent = new VBox(habitStatsLabel, habitStats);
        habitStats.setContent(habitStatsTotalContent);
    }
}
