package cs102groupproject.Client;

import java.time.LocalDateTime;

import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.Session;
import cs102groupproject.SharedObjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SessionPopupController {

    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField sessionNameTextField;
    @FXML
    private TextField sessionNoTextField;
    @FXML
    private TextField sessionLengthTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startTimeTextField;

    @FXML
    public void initialize(){
        WebSocketClient.addListener(
            ActionType.GROUP_SESSION_CREATED,
            payload -> {
                Session session = (Session) payload;
                System.out.println("✅ Solo session created: " + session.getName());
            }
        );
    }

    // Confirm button action
    @FXML
    private void onConfirm() {
        // data from UI
        String sessionName = sessionNameTextField.getText();
        int sessionNo = Integer.parseInt(sessionNoTextField.getText());

        String[] lengthParts = sessionLengthTextField.getText().split("/");
        int sessionLength = Integer.parseInt(lengthParts[0]);
        int breakLength = Integer.parseInt(lengthParts[1]);

        String[] timeParts = startTimeTextField.getText().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        LocalDateTime startDate =
                startDatePicker.getValue().atTime(hour, minute);

        User creator = ClientSession.getCurrentUser();

        Session sessionwithoutId = new Session(creator, sessionName, "SOLO_SESSION", sessionNo, sessionLength, breakLength, startDate);

        // send server with WebSocket
        ProtocolMessage msg = new ProtocolMessage(
                ActionType.CREATE_SOLO_SESSION,
                sessionwithoutId
        );

        WebSocketClient.send(msg);

        // Popup closing
        close();
    }

    // Cancel button action
    @FXML
    private void onCancel() {
        close();
    }

    // Close the popup window
    private void close() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
