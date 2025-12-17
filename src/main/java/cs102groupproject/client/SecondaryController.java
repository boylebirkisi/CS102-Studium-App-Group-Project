package cs102groupproject.client;

import java.io.IOException;

import javafx.fxml.FXML;

/**
 * Controller for secondary UI screens. Handles navigation and displays data received from the server.
 */
public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}