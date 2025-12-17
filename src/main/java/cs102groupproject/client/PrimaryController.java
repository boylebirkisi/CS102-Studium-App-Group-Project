package cs102groupproject.client;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * Controller for the main client UI. Sends user actions to the server and updates the interface based on server responses.
 */
public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
