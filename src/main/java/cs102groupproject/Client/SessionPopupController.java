package cs102groupproject.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SessionPopupController {

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void onConfirm() {
        close();
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
