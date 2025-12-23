package cs102groupproject.Client;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class SidePanelController {
    
    @FXML private StackPane contentArea;

    @FXML private Parent marketView;
    @FXML private Parent storageView;
    @FXML private Parent sessionView;

    @FXML
    public void initialize() {
        showMarket(); // default
    }

    @FXML
    private void showMarket() {
        marketView.toFront();
    }

    @FXML
    private void showStorage() {
        storageView.toFront();
    }

    @FXML
    private void showSessions() {
        sessionView.toFront();
    }

    @FXML
    private void handleCreateSession() {
        OfficeController.openSessionPopupStatic();
    }

    @FXML
    private void handleClose() {
        OfficeController.toggleSidePanelStatic();
    }
}
