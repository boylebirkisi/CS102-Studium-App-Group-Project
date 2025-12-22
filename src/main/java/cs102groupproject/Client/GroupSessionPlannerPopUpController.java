package cs102groupproject.Client;

import java.time.LocalDateTime;
import java.util.ArrayList;

import cs102groupproject.SharedObjects.GroupSession;
import cs102groupproject.SharedObjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class GroupSessionPlannerPopUpController {
    SocializationController ownerController;
    ClientSession manager;
    @FXML
    private TextField sessionNameTextField;
    @FXML
    private TextField sessionNoTextField;
    @FXML
    private TextField sessionLengthTextField;
    @FXML
    private RadioButton publicRadioButton;
    @FXML
    private RadioButton privateRadioButton;
    @FXML
    private TilePane friendsTilePane;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField timeTextField;

    @FXML
    private void initialize()
    {
    }

    public void setFields(SocializationController ownerController, ClientSession manager)
    {
        this.ownerController = ownerController;
        this.manager = manager;
        populateFriendsList();
    }

    private void populateFriendsList()
    {
        ArrayList<User> friendsList = ownerController.getFriendsList();
        for (int i = 0; i < friendsList.size(); i++)
        {
            User friend = friendsList.get(i);
            int Avatar = friend.getAvatar().toCharArray()[0];
            Label friendLabel = new Label(friend.getUsername() + " / " + friend.getDepartment());
            HBox friendBox = new HBox(Avatar, friendLabel);
            if (i % 2 == 0)
                ((VBox)friendsTilePane.getChildren().get(0)).getChildren().add(friendBox);
            else
                ((VBox)friendsTilePane.getChildren().get(1)).getChildren().add(friendBox);
        }
    }

    @FXML
    private void handleCreateSessionButton()
    {
        String sessionName = sessionNameTextField.getText();
        int sessionNo = Integer.parseInt(sessionNoTextField.getText());
        String[] lengthParts = sessionLengthTextField.getText().split("/");
        int sessionLength = Integer.parseInt(lengthParts[0]);
        int breakLength = Integer.parseInt(lengthParts[1]);
        boolean isPublic = publicRadioButton.isSelected();
        String[] timeParts = timeTextField.getText().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        LocalDateTime startDate = startDatePicker.getValue().atTime(hour, minute);
        //send notification to invited friends, they will be added to session if they accept.
        GroupSession session = new GroupSession(manager.getCurrentUser(), new ArrayList<>(),
            sessionName, "GroupSession", sessionNo, sessionLength,
            breakLength, startDate, isPublic);
        ownerController.closePopUp();
    }

    @FXML
    private void handleCancelButton()
    {
        ownerController.closePopUp();
    }
}
